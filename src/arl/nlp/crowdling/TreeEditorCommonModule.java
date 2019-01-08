/*
 *
 * This file is part of ARL CrowdTree and is subject to the following:
 * 
 * Copyright 2018 United States Government and Nhien Phan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *   
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */

package arl.nlp.crowdling;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import arl.common.io.InstanceWriter;
import arl.common.io.UTF8PrintWriter;
import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.ml.transition.decode.OneBestTransitionMachineDecoder;
import arl.ml.transition.train.Trainer;
import arl.nlp.parse.types.Parse;
import arl.nlp.crowdling.jsontypes.Sentence;

public class TreeEditorCommonModule implements Initializable {

	public static final String 	// file for saving off incoming annotations
								INIT_PARAM_ANNOTATION_RECORDER = "AnnotationRecorder",
								// (OPTIONAL) file for GET and POST requests and responses
								INIT_PARAM_LOG_FILE = "LogFile",
								
								// The parser trainer
								INIT_PARAM_TRAINER = "Trainer",
								// The parser
								INIT_PARAM_DECODER = "Decoder",
								
								// Data for training from (may be empty to start)
								INIT_PARAM_TRAINING_INSTANCES = "TrainingInstances",
								// Data for annotation (ArrayList or subclass (e.g., SentenceBundle))
								INIT_PARAM_ANNOTATION_INSTANCES = "InstancesForAnnotation",
								// If there are multiple submissions for the same sentence, controls if only the latest submission is used
								INIT_PARAM_TRAIN_ON_LATEST_ONLY = "TrainOnLatestSubmissionsOnly",
								
								// param max assignment to store (store what is provided to the worker)
								INIT_PARAM_MAX_ASSIGNMENTS_TO_STORE = "MaxAssignmentStorage";
	
	
	public final static String TRAINER_THREAD_NAME = "TrainerThread";
	
	private UTF8PrintWriter mIOLogger;
	private InstanceWriter<Parse> mAnnotationRecorder;
	
	private List<Parse> mToAnnotateList;
	
	private Map<Integer, LinkedList<Parse>> mSubmittedExamples;
	private boolean mTrainOnLatestOnly = false;
	
	private Vector<Parse> mTrainingInstances;
	
	private Trainer mTrainer;
	private OneBestTransitionMachineDecoder mDecoder;
	private Thread mTrainerThread;
	
	private AtomicInteger mSubmissionCounter = new AtomicInteger(0);
	
	// Since it is possible that an annotator may refresh their browser or come back to the assignment later,
	// it may be good to keep a copy of what was actually provided to them the first time.
	private int mMaxAssignmentsToTrack = 1000;
	private LinkedHashMap<String, Sentence> mAssignmentToProvidedJson = new LinkedHashMap<String, Sentence>() {
		private static final long serialVersionUID = 1L;
		@Override
		protected boolean removeEldestEntry(Map.Entry<String, Sentence> eldest) {
			return size()>=TreeEditorCommonModule.this.mMaxAssignmentsToTrack;
		}
	};
	
	@Override
	public void initialize(ArgMap argMap) throws InitializationException {
		Initializable.checkArgNames(argMap, new HashSet<>(Arrays.asList(INIT_PARAM_ANNOTATION_INSTANCES,
				INIT_PARAM_ANNOTATION_RECORDER,
				INIT_PARAM_LOG_FILE,
				INIT_PARAM_TRAINER,
				INIT_PARAM_DECODER,
				INIT_PARAM_TRAINING_INSTANCES,
				INIT_PARAM_TRAIN_ON_LATEST_ONLY,
				INIT_PARAM_MAX_ASSIGNMENTS_TO_STORE)));
		mTrainingInstances = (Vector<Parse>)argMap.get(INIT_PARAM_TRAINING_INSTANCES);
		mAnnotationRecorder = (InstanceWriter<Parse>)argMap.get(INIT_PARAM_ANNOTATION_RECORDER);
		mTrainOnLatestOnly = argMap.getBooleanValue(INIT_PARAM_TRAIN_ON_LATEST_ONLY, mTrainOnLatestOnly);
		mMaxAssignmentsToTrack = argMap.getIntegerValue(INIT_PARAM_MAX_ASSIGNMENTS_TO_STORE, mMaxAssignmentsToTrack);
		
		String ioOutputFile = argMap.getStringValue(INIT_PARAM_LOG_FILE);
		
		try {
			if(ioOutputFile != null) {
				mIOLogger = new UTF8PrintWriter(ioOutputFile, true);
			}
		}
		catch(IOException e) {
			throw new InitializationException(e);
		}
		
		// Load data for annotation
		mToAnnotateList = (List<Parse>)argMap.get(INIT_PARAM_ANNOTATION_INSTANCES);
		
		mTrainer = (Trainer)argMap.get(INIT_PARAM_TRAINER);
		if(mTrainer != null) {
			mTrainerThread = new Thread() {
				public void run() {
					try {
						mTrainer.execute();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			};
			mTrainerThread.setName(TRAINER_THREAD_NAME);
			mTrainerThread.start();
		}
		mDecoder = (OneBestTransitionMachineDecoder)argMap.get(INIT_PARAM_DECODER);
	}
	
	public void ioLog(String ioMessage) throws IOException {
		if(mIOLogger != null) {
			synchronized(mIOLogger) {
				mIOLogger.println(ioMessage);
				mIOLogger.flush();
			}
		}
	}
	
	public AtomicInteger getSubmissionCounter() {
		return mSubmissionCounter;
	}
	
	public InstanceWriter<Parse> getAnnotationRecorder() {
		return mAnnotationRecorder;
	}
	
	public List<Parse> getToAnnotateList() {
		return mToAnnotateList;
	}
	
	public OneBestTransitionMachineDecoder getDecoder() {
		return mDecoder;
	}
	
	public Vector<Parse> getTrainingInstances() {
		return mTrainingInstances;
	}
	
	public Map<Integer, LinkedList<Parse>> getSubmittedExamples() {
		return mSubmittedExamples;
	}
	
	public Thread getTrainerThread() {
		return mTrainerThread;
	}
	
	public boolean getTrainOnLatestOnly() {
		return mTrainOnLatestOnly;
	}
	
	public LinkedHashMap<String, Sentence> getAssignmentStorage() {
		return mAssignmentToProvidedJson;
	}
}
