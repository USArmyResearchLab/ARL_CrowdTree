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

!function() {
	"use strict";
	var depTree;

	function getQueryParam(variable) {
		var query = window.location.search.substring(1);
		var vars = query.split("&");
		for (var i = 0; i < vars.length; i++) {
			var pair = vars[i].split("=");
			if (pair[0] == variable) {
				return pair[1];
			}
		}
		return "";
	}

	var data = [
         	{
         		url: "qtest/0.html",
         		tooltip: "Qualification Test for Grammar Tree Creation/Editing",
         		tree: {"fulltext":"I threw him the ball .","sid":"0","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"I","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"threw","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"him","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"the","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"ball","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/1.html",
         		tooltip: "Introduction",
         		tree: {"fulltext":"I threw him the ball .","sid":"0","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"I","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"threw","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"him","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"the","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"ball","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/2.html",
         		tooltip: "Basics",
         		tree: {"fulltext":"The brown dog ate the sandwich .","sid":"1","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"The","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"brown","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"dog","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"ate","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"the","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"sandwich","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/3.html",
         		tooltip: "Prepositions and subordinating conjunctions",
         		tree: {"fulltext":"After she finished breakfast , she put the plates in the sink .","sid":"2","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"After","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"she","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"finished","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"breakfast","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"she","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"put","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"the","id":"7","oid":"7","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"plates","id":"8","oid":"8","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"in","id":"9","oid":"9","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"the","id":"10","oid":"10","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"sink","id":"11","oid":"11","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/4.html",
         		tooltip: "Verb chains, modals, and auxiliary verbs",
         		tree: {"fulltext":"The customers have been waiting at the front desk .","sid":"3","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"The","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"customers","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"have","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"been","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"waiting","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"at","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"the","id":"7","oid":"7","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"front","id":"8","oid":"8","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"desk","id":"9","oid":"9","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/5.html",
         		tooltip: "Coordinating conjunctions",
         		tree: {"fulltext":"The neighboring store processes film and prints photographs .","sid":"4","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"The","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"neighboring","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"store","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"processes","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"film","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"and","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"prints","id":"7","oid":"7","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"photographs","id":"8","oid":"8","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/6.html",
         		tooltip: "Adverbs and negation",
         		tree: {"fulltext":"Their annual expenses were publicly released in an online briefing .","sid":"5","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"Their","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"annual","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"expenses","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"were","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"publicly","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"released","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"in","id":"7","oid":"7","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"an","id":"8","oid":"8","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"online","id":"9","oid":"9","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"briefing","id":"10","oid":"10","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/7.html",
         		tooltip: "Apposition",
         		tree: {"fulltext":"The golden pothos , a very popular houseplant , can thrive in indirect sunlight .","sid":"6","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"The","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"golden","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"pothos","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"a","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"very","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"popular","id":"7","oid":"7","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"houseplant","id":"8","oid":"8","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"can","id":"10","oid":"10","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"thrive","id":"11","oid":"11","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"in","id":"12","oid":"12","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"indirect","id":"13","oid":"13","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"sunlight","id":"14","oid":"14","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[]}]}]}]}]}]}]}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/8.html",
         		tooltip: "The word \"to\"",
         		tree: {"fulltext":"She wants to listen to experimental music , but her friends dislike it .","sid":"7","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"She","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"wants","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"to","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"listen","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"to","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"experimental","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"music","id":"7","oid":"7","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"but","id":"9","oid":"9","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"her","id":"10","oid":"10","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"friends","id":"11","oid":"11","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"dislike","id":"12","oid":"12","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"it","id":"13","oid":"13","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[]}]}]}]}]}]}]}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/9.html",
         		tooltip: "The word \"that\"",
         		tree: {"fulltext":"That park has two fountains that were designed by a local landscape architect .","sid":"8","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"That","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"park","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"has","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"two","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"fountains","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"that","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"were","id":"7","oid":"7","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"designed","id":"8","oid":"8","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"by","id":"9","oid":"9","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"a","id":"10","oid":"10","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"local","id":"11","oid":"11","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"landscape","id":"12","oid":"12","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"architect","id":"13","oid":"13","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}]}]}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/10.html",
         		tooltip: "Words like \"said\"",
         		tree: {"fulltext":"'' We should buy that , '' he said eagerly .","sid":"9","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"We","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"should","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"buy","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"that","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"he","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"said","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"eagerly","id":"7","oid":"7","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/11.html",
         		tooltip: "Questions, relative clauses, and words like who, what, where ...",
         		tree: {"fulltext":"He does not fully understand how the pieces fit together .","sid":"10","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"He","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"does","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"not","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"fully","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"understand","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"how","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"the","id":"7","oid":"7","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"pieces","id":"8","oid":"8","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"fit","id":"9","oid":"9","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"together","id":"10","oid":"10","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/12.html",
         		tooltip: "Proper nouns",
         		tree: {"fulltext":"Jane P. Smith works for ABCsoft Inc.","sid":"11","dir":"ltr","root":{"text":"<<root>>","id":"0","oid":"0","parent":null,"pos":null,"gloss":null,"dep":null,"children":[{"text":"Jane","id":"1","oid":"1","parent":null,"pos":"NN","gloss":"null","dep":"ROOT","children":[{"text":"P.","id":"2","oid":"2","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"Smith","id":"3","oid":"3","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"works","id":"4","oid":"4","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"for","id":"5","oid":"5","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"ABCsoft","id":"6","oid":"6","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":[{"text":"Inc.","id":"7","oid":"7","parent":null,"pos":"NN","gloss":"null","dep":"dep","children":null}]}]}]}]}]}]}]}}
         	},
         	{
         		url: "qtest/13.html",
         		tooltip: "Submit qualification test",
         		tree: null
         	}
         ];

	if (getQueryParam("assignmentId") === "ASSIGNMENT_ID_NOT_AVAILABLE") {
		sessionStorage.scounter = 0;
	} else {
		sessionStorage.scounter = 1;
	}
	var visited = sessionStorage.scounter;

	var d3trees = new Array(data.length - 1);

	function loadTreeData() {
		var original = $("#hiddentree").val();
		if (original !== "" && sessionStorage.scounter != data.length - 1) {
			var treeData = data[sessionStorage.scounter].tree;
			var sText = treeData.fulltext;
	
			var textpara = $("#p_sentence")[0];

			while (textpara.hasChildNodes()) {
				textpara.removeChild(textpara.lastChild);
			}
			var emptySpace = document.createTextNode(" ");
			textpara.appendChild(emptySpace);
	
			var tokens = sText.split(" ");
		
			var arr = [];

			// add objects to array
			var createObject = function(obj) {
				for (var i = 0; i < obj.length; i++) {
					// put names in array according to oid (offset by 1)
					var token = {text: obj[i].text, oid: obj[i].oid}; 
					arr.push(token);

					// if object has children, add to arr
					if (obj[i].children) {
						createObject(obj[i].children);
					}
				}
			};

			createObject(treeData.root.children);

			arr.sort(function(a,b) {
				return a.oid - b.oid;
			});

			var tokMin = arr[0].oid;
			var curr = 0;

			// for each entry in sentence, check if multiple tokens
			tokens.forEach(function(entry, i) {
				// if punctuation, add without creating a span element
				if ((/^[,\/\.!?:;\-`()"\][{}]$/.test(entry)) ||
					/[`'-]{2}/.test(entry) || /\.{3}/.test(entry) || // ``, '', --, or ...
					(/'/.test(entry) && entry.length == 1 &&
					((curr < arr.length && arr[curr].text != "'") || (i >= arr.length)))) { // single quote
				// append leading whitespace if hyphen or opening parenthesis, brace, or bracket
					if (/[`\-"]/.test(entry) || /[({\[]/.test(entry)) {
						textpara.appendChild(document.createTextNode(" "));
					}
					textpara.appendChild(document.createTextNode(entry));
				}
				// create token
				else {
					// append leading whitespace between entries if not single quote or percent
					if (!/['%]/.test(entry)) {
						textpara.appendChild(document.createTextNode(" "));
					}

					// if entry is one token
					// (token matches arr at current index and fully delineated by leading/trailing whitespace)
					if (arr[curr].text === entry) {
						var span = document.createElement('span');
						var text = document.createTextNode(entry);
						span.appendChild(text);
						span.setAttribute('id', 'tok'+(arr[curr].oid)); // index is oid

						// if number, set text direction to ltr (this is only needed for arabic)
						if (/[\d]/.test(entry)) {
							span.setAttribute('dir', 'ltr');
						}
						textpara.appendChild(span);
						curr++;
					}
					// if entry is multiple tokens (displayed as one "word" with no leading/trailing whitespace in arabic),
					// print multiple span elements without whitespace
					else {
						var len = 0;
						// continue until total length of entry is reached
						while (len < entry.length) {
							len += arr[curr].text.length;
							var span = document.createElement('span');
							var text = document.createTextNode(arr[curr].text);
							span.appendChild(text);
							span.setAttribute('id', 'tok'+(arr[curr].oid));
							textpara.appendChild(span);
							curr++;
						}
					}
				}
			});
		} 
		else if (sessionStorage.scounter != data.length - 1) {
			alert("Missing tree data!");
		};
		
		$("#leftbar").load(data[sessionStorage.scounter].url);
		$('#leftbar').animate({
			scrollTop: $('#head')
		}, 'slow');
		
		if (sessionStorage.scounter == data.length - 1) {
			$("#submitbutton").removeClass("disabled");
			document.getElementById("submitbutton").removeAttribute("disabled");
			$("#submitbutton").attr("data-original-title", "Submit qualification test for grading.");
			$("#p_sentence").empty();
			$("#p_sentence").html("<p>That's all! You can still review/edit your work. <br/> When you're done, hit the <b>Submit Test!</b> button. <br/> We'll try to score your submission promptly. <i>Thanks!!!</i></p>");
			$("#recenter").css("display", "none");
			$("#treesvg").empty();
		} else {
			$("#recenter").css("display", "block");
			if(!d3trees[sessionStorage.scounter]) {
				var d3root = d3.hierarchy(treeData.root, function(d) { return d.children; });
				d3trees[sessionStorage.scounter] = {"sid":treeData.sid,
													"root":d3root}; 
			}
			var d3TreeRoot = d3trees[sessionStorage.scounter].root;
			
			if(!depTree) depTree = new dtree.DTree();
			depTree.generateSvg(
								d3TreeRoot,//treeData.root, 
								  // SVG element
								  $("#treesvg")[0],
								  $(document).width(),
								  $(document).height(),
								  $('#tok'+tokMin)[0].getBoundingClientRect().left+depTree.getNodeSize(),
								  130+depTree.getNodeSize()			
			);
		}
	};

	$(document).ready(function(){
		for (var i = 1; i < data.length; i++) {
			$("#p" + i).attr("title", data[i].tooltip);
		}
	
		/* Set up submission to Turk */
		$("#qtest_form").attr("method", "post");
		$("#qtest_form").attr("action", (getQueryParam("turkSubmitTo").includes("sandbox")?"https://workersandbox.mturk.com/mturk/externalSubmit":"https://www.mturk.com/mturk/externalSubmit"));	
	
		$('[data-toggle="tooltip"]').tooltip();
	
		$("#submitbutton").tooltip('hide').attr('data-original-title',
		"You must complete all trees before submitting the qualification test.");
		/*$("#submitbutton").addClass("disabled").attr('type', "button");*/
		$(".page").click(function() {
			var value = parseInt($(this).text());
			if(value <= visited && value != sessionStorage.scounter) {
				$("#p" + sessionStorage.scounter).removeClass("active");
				sessionStorage.scounter = value;
				$("#p" + sessionStorage.scounter).addClass("active");
				loadTreeData();
			}
			document.activeElement.blur();
		});
	
		$("#prevclick").click(function() {
			if (sessionStorage.scounter > 1) {
				$("#p" + sessionStorage.scounter).removeClass("active");
				sessionStorage.scounter--;
				$("#p" + sessionStorage.scounter).addClass("active");
				loadTreeData();
			}
		});
	
		$("#nextclick").click(function() {
			if (sessionStorage.scounter < data.length - 1) {
				$("#p" + sessionStorage.scounter).removeClass("active");
				sessionStorage.scounter++;
				$("#p" + sessionStorage.scounter).addClass("active");
				$("#p" + sessionStorage.scounter).removeClass("disabled");
				loadTreeData();
				visited = Math.max(visited,sessionStorage.scounter);
			}
		});
	
		$("#submitbutton").click(function(){
			$("#submitbutton").attr("disabled", "disabled");
			var sendBack = {
				sentences: d3trees,
				workerId: document.getElementById("workerId").value,
				hitId: document.getElementById("hitId").value,
				assignmentId: document.getElementById("assignmentId").value,
				notes: document.getElementById("notes").value
			}; 
			var json = JSON.stringify(sendBack, function(k, v) {
				if (k === 'parent' ||
					//k === 'post' ||
					//k === 'pre' ||
					k === 'x' ||
					k === 'y' ||
					k === 'x0' ||
					k === 'y0' ||
					k === 'depth' ||
					k === 'id' ||
					k === 'figure' ||
					k === 'width' ||
					k === 'height' ||
					k === 'fulltext' ||
					k === 'previousSubmission' ||
					k === 'dir' ||
					k === 'text' ||
					k === 'pos' ||
					k === 'gloss' ||
					k === 'dep') {
					return undefined;
				}
			
				return v;
			});
			document.getElementById("results").setAttribute("value", json);
		
			$.ajax({
				url: "/active/qtest",
				type: 'post',
				data: json
			})
			.done(
				function() {
					$("#qtest_form").attr("action", (getQueryParam("turkSubmitTo").includes("sandbox")?"https://workersandbox.mturk.com/mturk/externalSubmit":"https://www.mturk.com/mturk/externalSubmit"));
					$("#qtest_form").attr("method", "post");
					$("#qtest_form").submit();
				}
			);
		});
	
		$("#recenter").click(function(){
			loadTreeData();
		});
	
		if (getQueryParam("assignmentId") === "ASSIGNMENT_ID_NOT_AVAILABLE") {
			$("#treesvg").css("pointer-events", "none");
			$("#buttons").hide();
		} else {
			document.getElementById("assignmentId").setAttribute("value", getQueryParam("assignmentId"));
			document.getElementById("workerId").setAttribute("value", getQueryParam("workerId"));
			document.getElementById("hitId").setAttribute("value", getQueryParam("hitId"));
		}
		
		loadTreeData();
	});

}();
