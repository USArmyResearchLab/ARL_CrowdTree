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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.exception.ObjectAlreadyExistsException;
import com.amazonaws.mturk.util.ClientConfig;*/
import com.fasterxml.jackson.databind.ObjectMapper;

import arl.common.io.UTF8BufferedFileReader;
import arl.common.io.UTF8PrintWriter;
import arl.common.util.IOUtils;
import arl.nlp.crowdling.jsontypes.QualTestSentence;
import arl.nlp.crowdling.jsontypes.D3TreeNodeWrapper;
import arl.nlp.crowdling.jsontypes.QualTestSubmissionObject;

public class QualTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public final static String INIT_PARAM_CONFIG_FILE = "CONFIG_FILE";
	
	public final static String INIT_PARAM_MTURK_ACCESS_KEY = "MTURK_ACCESS_KEY",
							   INIT_PARAM_MTURK_SECRET_KEY = "MTURK_SECRET_KEY",
							   INIT_PARAM_QUALIFICATION_TYPE_ID = "QUALIFICATION_TYPE_ID",
							   INIT_PARAM_ANSWER_KEY_FILE = "ANSWER_KEY_FILE",
							   INIT_PARAM_SANDBOX_MODE = "SANDBOX_MODE",
							   INIT_PARAM_RECORD_FILE = "JSON_RECORD_FILE";
	
	public final static String APP_REQUESTER_SERVICE = "REQUESTER_SERVICE",
							   APP_RECORDS_WRITER = "RECORDS_WRITER",
							   APP_ANSWER_KEY = "ANSWER_KEY",
							   APP_QUALIFICATION_TYPE_ID = "QUALIFICATION_TYPE_ID",
							   APP_TOTAL_ARCS_IN_KEY = "TOTAL_ANSWER_ARCS";
	
	
	
	@Override
	public void init() throws ServletException {
		System.err.println("QualTestServlet: init() called");
		
		try {
			String configFile = getServletConfig().getInitParameter(INIT_PARAM_CONFIG_FILE);
			
			Properties configProperties = new Properties();
			configProperties.load(new UTF8BufferedFileReader(configFile));
			
			String accessKey = configProperties.getProperty(INIT_PARAM_MTURK_ACCESS_KEY);
			String secretKey = configProperties.getProperty(INIT_PARAM_MTURK_SECRET_KEY);
			String qualificationTypeId = configProperties.getProperty(INIT_PARAM_QUALIFICATION_TYPE_ID);
			String answerKeyFile = configProperties.getProperty(INIT_PARAM_ANSWER_KEY_FILE);
			String sandboxModeString = configProperties.getProperty(INIT_PARAM_SANDBOX_MODE);
			String recordFile = configProperties.getProperty(INIT_PARAM_RECORD_FILE);
			String qualificationId = configProperties.getProperty(INIT_PARAM_QUALIFICATION_TYPE_ID);
			
			getServletContext().setAttribute(APP_QUALIFICATION_TYPE_ID, qualificationId);
			
			// This if statement *should not* be necessary
			if(getServletContext().getAttribute(APP_REQUESTER_SERVICE) == null) {
				if(answerKeyFile != null) {
					File answerFile = new File(answerKeyFile);
					String answerKeyContents = IOUtils.readUTF8FileAsString(answerFile);
					ObjectMapper mapper = new ObjectMapper();
					QualTestSubmissionObject answerObject = 
							mapper.readValue(answerKeyContents, QualTestSubmissionObject.class);
					Map<String, Set<String>> answerKey = new HashMap<String, Set<String>>();
					int totalAnswerArcs = 0;
					for(QualTestSentence sentence : answerObject.getSentences()) {
						if(sentence != null) {
							int sentenceId = Integer.parseInt(sentence.getSid());
							Set<String> keyForSentence = new HashSet<>();
							answerKey.put(""+sentenceId, keyForSentence);
							populateSentenceKey(keyForSentence, sentence.getRoot(), true);
							totalAnswerArcs+=keyForSentence.size();
						}
					}
					getServletContext().setAttribute(APP_TOTAL_ARCS_IN_KEY, totalAnswerArcs);
					getServletContext().setAttribute(APP_ANSWER_KEY, answerKey);
				}
				
				UTF8PrintWriter writer = new UTF8PrintWriter(recordFile, true);
				getServletContext().setAttribute(APP_RECORDS_WRITER, writer);
				
				// Create the RequesterService object for accessing the API
				/*ClientConfig clientConfig = new ClientConfig();
				clientConfig.setServiceURL(!sandboxModeString.equals("false")
					?ClientConfig.SANDBOX_SERVICE_URL:ClientConfig.PRODUCTION_SERVICE_URL);
				clientConfig.setAccessKeyId(accessKey);
				clientConfig.setSecretAccessKey(secretKey);
				clientConfig.setRetriableErrors(new HashSet<>(Arrays.asList("Server.ServiceUnavailable")));
				clientConfig.setRetryAttempts(10);
				clientConfig.setRetryDelayMillis(1000);
				
				RequesterService service = new RequesterService(clientConfig);
				if(service.getQualificationType(qualificationTypeId) == null) throw new NullPointerException();
				
				getServletContext().setAttribute(APP_REQUESTER_SERVICE, service);*/
			}
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		System.err.println("QualTestServlet: init() completed");
	}

	@Override
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
							  throws ServletException, IOException {
		// Set response content type
				response.setContentType("text/html");

				// Actual logic goes here.
				PrintWriter out = response.getWriter();
				out.println("<h1>Hello World</h1>");
	}
	
	@Override
	public void doPost(HttpServletRequest request, 
					   HttpServletResponse response)
			throws ServletException, IOException {
		try {
			System.err.println("QTS: doPost");
			ObjectMapper om = new ObjectMapper();
			// Read content (JSON object as a string)
			String requestContent = IOUtils.readUTF8StreamAsString(request.getInputStream(), "\n");
			UTF8PrintWriter recordsWriter = (UTF8PrintWriter)getServletContext().getAttribute(APP_RECORDS_WRITER);
			synchronized(recordsWriter) {
				recordsWriter.println(System.currentTimeMillis() + "\t" + requestContent);
				recordsWriter.flush();
			}
		
			System.err.println("QTS: interpreting submission");
			QualTestSubmissionObject submission = 
				om.readValue(requestContent, QualTestSubmissionObject.class);
			if(!submission.getWorkerId().matches("[A-Z0-9]+")||
			   !submission.getAssignmentId().matches("[A-Z0-9]+")||
			   !submission.getHitId().matches("[A-Z0-9]+")) {
				System.err.println("QTS: Bad data received. WorkerId, HitID, and AssignmentID must be alphanumeric: " + submission.getWorkerId() + " " + submission.getAssignmentId() + " " + submission.getHitId());
				// return now, don't pass Go
				return;
			}
			
			List<QualTestSentence> sentences = submission.getSentences();
		
			// NOTE: It might be nice to have some way of determining if the submissions are not what we are expecting
			// and then generate a notification!!!
		
			System.err.println("QTS: calculating score");
			int score = -999;
		
			Map<String, Set<String>> sentenceToArcs = (Map<String, Set<String>>)getServletContext().getAttribute(APP_ANSWER_KEY);
			if(sentenceToArcs != null) {
				int totalArcsInKey = (Integer)getServletContext().getAttribute(APP_TOTAL_ARCS_IN_KEY);
				int arcsCorrect = 0;
				//int arcsSubmitted = 0;
				for(QualTestSentence sentence : sentences) {
					if(sentence != null) {
						List<String> arcList = new ArrayList<>();
						populateSentenceKey(arcList, sentence.getRoot(), true);
						Set<String> keyForSentence = sentenceToArcs.get(""+sentence.getSid());
						if(keyForSentence != null) {
							//arcsSubmitted+=arcList.size();
							for(String proposedArc : arcList) {
								if(keyForSentence.contains(proposedArc)) arcsCorrect++;
							}
						}
						else {
							System.err.println("QTS: no key for sentence id: " + sentence.getSid());
						}
					}
				}
				System.err.println("QTS: score: " + arcsCorrect + "/" + totalArcsInKey);
				score = (int)(arcsCorrect / (double)totalArcsInKey * 100);
			}
		
			String qualificationTypeId = (String)getServletContext().getAttribute(APP_QUALIFICATION_TYPE_ID);
			String workerId = submission.getWorkerId();
			/*
			RequesterService service = (RequesterService)getServletContext().getAttribute(APP_REQUESTER_SERVICE);
			System.err.println("QTS: assigning qualification: " + qualificationTypeId + " to " + workerId + " w score " + score);
			synchronized(service) {
				try {
					service.assignQualification(qualificationTypeId, workerId, score, true);
				}
				catch (ObjectAlreadyExistsException e) {				
					int oldScore = service.getQualificationScore(qualificationTypeId, workerId).getIntegerValue();
					if (score > oldScore) {
						System.err.println("QTS: updating qualification " + qualificationTypeId + " for " + workerId + " to score: " + score);
						service.updateQualificationScore(qualificationTypeId, workerId, score);
					}
				}
			}*/
			System.err.println("QTS: finished doPost");
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void destroy() {
	}
	
	private void populateSentenceKey(Collection<String> arcs, D3TreeNodeWrapper rootNode, boolean verbose) {
		LinkedList<D3TreeNodeWrapper> nodesToProcess = new LinkedList<>();
		nodesToProcess.add(rootNode);
		while(nodesToProcess.size() > 0) {
			D3TreeNodeWrapper p = nodesToProcess.removeFirst();
			int pOid = Integer.parseInt(p.getData().getOid());
			List<D3TreeNodeWrapper> children = p.getChildren();
			if(children != null) {
				for(D3TreeNodeWrapper child : children) {
					arcs.add(pOid+"->"+child.getData().getOid());
					nodesToProcess.add(child);
				}
			}
		}
		
	}
	
}
