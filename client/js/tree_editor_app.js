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

var TreeEditorApp = (function() {
	"use strict";
	
	var originalTree,
		d3TreeRoot,
		// CoNLL-U comment lines
		comments,
		// Variable for storing the JavaScript object corresponding to the
		// node in the tree that was clicked on for purposes of setting the
		// dependency label
		clickedDepNode,
		clickedPosNode,
		// id of first token
		tokMin,
		/* Part of selection code
		 * nodeSelected = false,
		 */
		depTree = new dtree.DTree();
	
	var dep_container = $("#dep-container");
	var pos_container = $("#pos-container");
	var dep_menu = $("#dep-menu");
	var pos_menu = $("#pos-menu");

	function getQueryParam(param) {
		var pairs = window.location.search.substring(1).split("&");
		for(var i = 0; i < pairs.length; i++) {
			var split = pairs[i].split("=");
			if (split[0] === param) {
				return split[1];
			}
		}
		return false;
	}

	sessionStorage.scounter = getQueryParam("tree");
	sessionStorage.currTok;
	
	function isMTurkMode() {
		return getQueryParam("hitId") ? true : false;
	}

	function init() {
		$.ajax({
			url : "guidelines.html",
			context : document.body
		}).done(function(response) {
			help_div.html(response);
		});
		
		var help_div = $("#div_help");
		$("#btn_recenter").on("click", function() {
			//depTree.setTransform(d3.zoomIdentity);
			//depTree.resetView(1);
			//depTree.attr("transform","translate(0,0)");
			console.log($('#tok1')[0].getBoundingClientRect().left+ depTree.getNodeSize());
			depTree.resetView($('#tok1')[0].getBoundingClientRect().left+ depTree.getNodeSize());
		});
		$("#btn_flatten").on("click", flattenTree);
		$("#btn_showguide").on("click", function() {help_div.toggle();});
		$("#btn_submit").on("click", postDataToServer);
		
		// Dependency label editor

		if (typeof sessionStorage.depWidth != "undefined" && typeof sessionStorage.depHeight != "undefined") {
			document.getElementById("dep-container").style.width = sessionStorage.depWidth + "px";
			document.getElementById("dep-container").style.height = sessionStorage.depHeight + "px";
			document.getElementById("dep-menu").style.width = sessionStorage.depWidth - 20 + "px";
			document.getElementById("dep-menu").style.height = sessionStorage.depHeight - 15 + "px";
		}

		if (typeof sessionStorage.posWidth != "undefined" && typeof sessionStorage.posHeight != "undefined") {
			document.getElementById("pos-container").style.width = sessionStorage.posWidth + "px";
			document.getElementById("pos-container").style.height = sessionStorage.posHeight + "px";
			document.getElementById("pos-menu").style.width = sessionStorage.posWidth - 20 + "px";
			document.getElementById("pos-menu").style.height = sessionStorage.posHeight - 15 + "px";
		}

		dep_container.resizable(
			{
			minWidth : 100,
			minHeight : 120,
			containment : "body",
			alsoResize: "#dep-menu"
			});

		pos_container.resizable(
			{
			minWidth : 100,
			minHeight : 120,
			containment : "body",
			alsoResize: "#pos-menu"
			});
		
		$.each(dep_labels, function(i, item) {
			var li = document.createElement("li");
			var div = document.createElement("div");
			div.setAttribute("name", item.value);
			var text = document.createTextNode(item.value + " - " + item.description);
			div.appendChild(text);
			li.appendChild(div);
			dep_menu.append(li);
		});

		$.each(pos_labels, function(i, item) {
			var li = document.createElement("li");
			var div = document.createElement("div");
			div.setAttribute("name", item.value);
			var text = document.createTextNode(item.value + " - " + item.description);
			div.appendChild(text);
			li.appendChild(div);
			pos_menu.append(li);
		});

		dep_menu.menu();
		pos_menu.menu();
		

		$(document).on("click", function(event) { 
			if(dep_container.is(":visible")) {
				sessionStorage.depWidth = document.getElementById('dep-container').offsetWidth;
				sessionStorage.depHeight = document.getElementById('dep-container').offsetHeight;
			}

		    if(!$(event.target).closest('.dep').length) {
		        if(dep_container.is(":visible")) {
		            dep_container.hide();
		        }
		    }
			if(!$(event.target).closest('.node').length) {
				if(!dep_container.is(":visible") && !pos_container.is(":visible")) {
					/* Part of selection code 
					$("#selected_node").hide();
					nodeSelected = false;
					*/
				}
			}

			if(pos_container.is(":visible")) {
				sessionStorage.posWidth = document.getElementById('pos-container').offsetWidth;
				sessionStorage.posHeight = document.getElementById('pos-container').offsetHeight;
			}

		    if(!$(event.target).closest('.pos').length) {
		        if(pos_container.is(":visible")) {
		            pos_container.hide();
		        }
		    }
		});

		$("#dep-menu .ui-menu-item-wrapper").on("click", function(d) {
			// cached node bounds out-of-date
			clickedDepNode.width = undefined;
			d3.select(clickedDepNode.figure)
				.select(".dep")
				.text(function(d) {	
					var active = dep_menu.attr("aria-activedescendant");
					return clickedDepNode.data.dep = $("#" + active).attr("name");
				});
			dep_container.hide();
			// layout tree to reflect modified bounding box of node
			depTree.updateTree(d3TreeRoot);
			});

		$("#pos-menu .ui-menu-item-wrapper").on("click", function(d) {
			clickedPosNode.width = undefined;
			d3.select(clickedPosNode.figure)
				.select(".pos")
				.text(function(d) {	
					var active = pos_menu.attr("aria-activedescendant");
					return clickedPosNode.data.pos = $("#" + active).attr("name");
				});
			pos_container.hide();
			depTree.updateTree(d3TreeRoot);
			});
	};

	function attachDescendants(node, newAttachSite) {
		if (node.children) {
			while (node.children.length > 0) {
				// Remove first node and attach it elsewhere
				var removeNode = node.children.shift();
				removeNode.parent = newAttachSite;
				newAttachSite.children.push(removeNode);
				attachDescendants(removeNode, newAttachSite);
			}
		}
	};
	
	function flattenTree() {
		if (d3TreeRoot.children) {
			// recursively attach descendants to root
			var rChildren = d3TreeRoot.children.length;
			for (var i = 0; i < rChildren; i++) {
				attachDescendants(d3TreeRoot.children[i], d3TreeRoot);
			}
			d3TreeRoot.children.sort(function(a, b) {
				return a.data.oid - b.data.oid;
			});
		}
		depTree.updateTree(d3TreeRoot);
	};

	function processJsonTreeString(httpGetResponse) {
			// Save off a copy of original to submit back to server
			originalTree = JSON.parse(httpGetResponse).sentence;
			// Create a copy that'll get wrapped by d3
			var treeData = JSON.parse(httpGetResponse).sentence;
			var sText = treeData.fulltext;

			var textpara = $("#p_sentence")[0];
			textpara.setAttribute('dir', treeData.dir);

			while (textpara.hasChildNodes()) {
				textpara.removeChild(textpara.lastChild);
			}

			var all = [];
			var toks = [].concat(treeData.root.children);
			while(toks.length > 0) {
				var tok = toks.shift();
				all.push(tok);
				if(tok.children) {
					toks = toks.concat(tok.children);
				}
			}
			all.sort(function(a, b) {
				return a.oid - b.oid;
			});
			tokMin = undefined;
			var numToks = all.length;
			for(var i = 0; i < numToks; i++) {
				var entry = all[i];
				if(entry.punct) {
					textpara.appendChild(document.createTextNode(entry.text));	
					
					if(!entry.noSpaceAfter) {
						textpara.appendChild(document.createTextNode(" "));
					}
				}
				else {
					var span = document.createElement('span');
					var text = document.createTextNode(entry.text);
					span.appendChild(text);
					span.setAttribute('id', 'tok' + entry.oid);
					if(!tokMin) {
						tokMin = entry.oid;
					}
					// if number, set text direction to ltr (this is
					// only needed for left-to-right scripts)
					if (/[\d]/.test(entry.text)) {
						span.setAttribute('dir', 'ltr');
					}
					textpara.appendChild(span);
					
					if(!entry.noSpaceAfter) {
						textpara.appendChild(document.createTextNode(" "));
					}
				}
			}

			//depTree = new dtree.DTree();
			depTree.setOrientation(treeData.dir)
					.setShowDep(true)
					.setShowPos(false)
					//.setLevelHeight(90)
					//.setAnimationDuration(400)
					.setDepClickCallback(function(d) {
						// disambiguate drag from click
						if (d3.event.defaultPrevented)
							return;
						clickedDepNode = d;
						var menuX = d3.event.pageX;
						var menuY = d3.event.pageY;
						if (menuX > window.innerWidth - 300) {
							menuX -= 300;
						}
						if (menuY > window.innerHeight - 230) {
							menuY -= 230;
						}
						// Detaching and reappending menu to avoid apparent redraw issue in Firefox
						dep_menu.detach();
						dep_container.css("left", menuX + "px");
						dep_container.css("top", menuY + "px");
						dep_menu.appendTo(dep_container[0]);
						dep_container.css("display", "block");
						
					})
					.setPosClickCallback(function(d) {
						if (d3.event.defaultPrevented)
							return;
						clickedPosNode = d;
						var menuX = d3.event.pageX;
						var menuY = d3.event.pageY;
						if (menuX > window.innerWidth - 300) {
							menuX -= 300;
						}
						if (menuY > window.innerHeight - 230) {
							menuY -= 230;
						}
						// Detaching and reappending menu to avoid apparent redraw issue in Firefox
						pos_menu.detach();
						pos_container.css("left", menuX + "px");
						pos_container.css("top", menuY + "px");
						pos_menu.appendTo(pos_container[0]);
						pos_container.css("display", "block");
					})
					.setEndOfUpdateCallback(
							function() {
								if(getQueryParam("assignmentId") !== "ASSIGNMENT_ID_NOT_AVAILABLE") {
									var postBtn = $("#btn_submit");
									var disable = (d3TreeRoot.children && d3TreeRoot.children.length > 1);
								
									postBtn.prop("disabled", disable)
											.toggleClass("disabled", disable)
											.tooltip('hide')
											.attr('data-original-title',
												disable?"At most one word may be connected to the root. Please correct."
														:"Submit the completed tree");
								}
							}
					)
					.setNodeDragCallback(function(d) {
						d3.select("#tok" + d.data.oid).attr("class", "highlighttok");
						dep_container.hide();
						pos_container.hide();
					})
					.setNodeMouseoverCallback(function(d) {
						if (!depTree.isDraggingNode(d)) {
							// highlight associated text
							d3.select("#tok" + d.data.oid)
								.attr("class",
									depTree.isDragActive() ? "highlightdrop" : "highlighttok");
							sessionStorage.currTok = d.data.oid;
						}
					})
					.setNodeMouseoutCallback(function(d) {
						if (!depTree.isDraggingNode(d)) {
							d3.select("#tok" + d.data.oid).attr("class", "normaltok");
						}
					})
					.setNodeClickCallback(function(d){
						var nodes = depTree.getNodes();
						var data;
						for (var i = 0; i < nodes.length; i++) {
							if (nodes[i].data.oid == sessionStorage.currTok) {
								data = nodes[i].data;
							}
						}
						/* Part of selection code
						$("#selected_node").show();
						$("#selected_node").html("<hr><h4>Selected node</h4><strong>" + data.text +
								"</strong><ul><li><strong>oid: </strong>" + data.oid +
								"</li><li><strong>dep: </strong>" + data.dep +
								"</li><li><strong>pos: </strong>" + data.pos +
								"</li><li><strong>pos2: </strong>" + data.pos2 +
								"</li><li><strong>lemma: </strong>" + data.lemma +
								"</li><li><strong>lexical root: </strong>" + data.lroot +
								"</li><li><strong>gloss: </strong>" + data.gloss +
								"</li><li><strong>noSpaceAfter: </strong>" + data.noSpaceAfter +
								"</li></ul>");
						$(".dtree-interactive g.node").attr("style","");
						$(this).attr("style","font-weight: bold; stroke-width: 2px;");
						nodeSelected = true;*/
					})
					.setEndDragCallback(function(d) {
						d3.select("#tok" + d.data.oid).attr("class", "normaltok");
					})
					.setZoomCallback(function() {
						dep_container.hide();
						pos_container.hide();
					});
			d3TreeRoot = d3.hierarchy(treeData.root, function(d) {
				var nonPunctChildren;
				if(d.children) {
					nonPunctChildren = [];
					var dToProcess = [];
					dToProcess.push(d.children);
					while(dToProcess.length > 0) {
						var array = dToProcess.shift();
						var len = array.length;
						for(var i = 0; i < len; i++) {
							var node = array[i];
							if(!node.punct) {
								nonPunctChildren.push(node);
							}
							else {
								if(node.children) {
									dToProcess.push(node.children);
								}
							}
						}
					}
				}
				return nonPunctChildren;
			});
			comments = Array.from(treeData.comments);
			depTree.generateSvg(d3TreeRoot,
								// SVG element
								$("#svg_tree")[0], 
								$(document).width(), 
								$(document).height(),
								$('#tok' + tokMin)[0].getBoundingClientRect().left+ depTree.getNodeSize(), 
								130 + depTree.getNodeSize());
	};

	function requestTreeFromServer() {
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				$("#btn_sid").text("" + sessionStorage.scounter);

				processJsonTreeString(xhr.responseText);

				var submitButton = $("#btn_submit");
				if (getQueryParam("assignmentId") === "ASSIGNMENT_ID_NOT_AVAILABLE") {
					// treesvg.style.pointerEvents="none";
					submitButton.html("HIT not accepted yet").prop("disabled", true).attr('data-original-title', "HIT must be accepted before submitting.");

					$("#intro").css("display", "block");
					
					if (typeof SVGRect == "undefined") {
						$("alert").html("Your browser does not support SVG functionality, which is needed to take this HIT. SVG is not supported by Internet Explorer 8 and earlier versions. Please switch to a different browser.");
					} else {
						$("alert").css("display", "none");
					}
				} else {
					submitButton.html("Submit HIT").prop("disabled", false);
					$("#assignmentId").attr("value", getQueryParam("assignmentId"));
					$("#workerId").attr("value", getQueryParam("workerId"));
					$("#hitId").attr("value", getQueryParam("hitId"));
				}
			}
		};
		
		xhr.open("GET", "/arlcrowdtree/trees?tree=" + encodeURIComponent("" + sessionStorage.scounter)
				+ "&turkSubmitTo=" + encodeURIComponent(getQueryParam("turkSubmitTo"))
				+ "&hitId=" + encodeURIComponent(getQueryParam("hitId"))
				+ "&assignmentId=" + encodeURIComponent(getQueryParam("assignmentId"))
				+ "&workerId=" + encodeURIComponent(getQueryParam("workerId")), true);
		xhr.send();
		
		if(!isMTurkMode()){
			document.getElementById("notes").value = "";
			if (getQueryParam("showTreeId")) {
				var btn_sid = $("#btn_sid");
				btn_sid.show();
				btn_sid.empty();
				btn_sid.html(parseInt("" + sessionStorage.scounter));
			}
		}
	}

	function postDataToServer() {
		/* disable submit button after click */
		$("#btn_submit").prop("disabled", true).addClass("disabled");
		
		var browser = "undefined";
		// browser detection code goes here
		
		var sendback = {
			root : d3TreeRoot,
			comments : comments,
			originalSentence : originalTree,
			notes : document.getElementById("notes").value,
			browser : browser,
			screenHeight: window.screen.availHeight * window.devicePixelRatio,
			screenWidth: window.screen.availWidth * window.devicePixelRatio
		};
		// if (isMTurkMode()) {
			sendback.workerId = getQueryParam("workerId");
			sendback.hitId = getQueryParam("hitId");
			sendback.assignmentId = getQueryParam("assignmentId");
		//}
		var json = JSON.stringify(sendback, function(k, v) {
			if (k === 'parent' ||
				k === 'x' || 
				k === 'y' || 
				k === 'x0'|| 
				k === 'y0' || 
				k === 'depth' || 
				k === 'id'|| 
				k === 'figure' || 
				k === 'width' || 
				k === 'height') {
				return undefined;
			}
			return v;
		});
		document.getElementById("submissionJson").setAttribute("value", json);
		$.ajax({
				url : "/arlcrowdtree/trees",
				type : 'post',
				data : json
				})
				.done(function() {
							if (isMTurkMode()) {
								var form = $("#f_main");
								form.attr("action",
										  (getQueryParam("turkSubmitTo")
												  .includes("sandbox") ? "https://workersandbox.mturk.com/mturk/externalSubmit"
												  : "https://www.mturk.com/mturk/externalSubmit"));
								form.submit();
							} else {
								sessionStorage.scounter++;
								requestTreeFromServer();
								history.pushState("", "", "/arlcrowdtree/tree_editor_app.html?tree=" + sessionStorage.scounter);
								
							}
						});
	};

	$(document).ready(function() {
		init();
		requestTreeFromServer();
		$('[data-toggle="tooltip"]').tooltip();
	});

	/* Part of selection code
	$(document).on("click", function(e) {
		if (nodeSelected) {
			nodeSelected = false;
		} else {
			$(".dtree-interactive g.node").attr("style","");
		}
	});*/
	
	return {
		getNodes: function() {
			return depTree.getNodes();
		}
	};
	
}());
