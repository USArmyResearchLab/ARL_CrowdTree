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

/*	
 	::BEGIN NOTE::
	   Some of the code in this file is based upon code which had the following copyright
	   notice, conditions, and disclaimer attached. In keeping with the list of 
	   conditions, the copyright notice, conditions, and disclaimer are retained in this 
	   file. Note that this file is substantially different from the file that this 
	   copyright, list of conditions, and disclaimer were originally attached to.
	::END NOTE::
	
 	Copyright (c) 2015, Alexa Little
    All rights reserved.

	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met:

	1. Redistributions of source code must retain the above copyright notice, this 
	list of conditions and the following disclaimer.

	2. Redistributions in binary form must reproduce the above copyright notice, 
	this list of conditions and the following disclaimer in the documentation 
	and/or other materials provided with the distribution.

	3. Neither the name of the copyright holder nor the names of its contributors
	may be used to endorse or promote products derived from this software without 
	specific prior written permission.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
	AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
	IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
	INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
	BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
	DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
	OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
	NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
	EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/*
	::BEGIN NOTE::
	   Some of the code in this file is based upon code which had the following copyright
	   notice, conditions, and disclaimer attached. In keeping with the list of 
	   conditions, the copyright notice, conditions, and disclaimer are retained in this 
	   file. Note that this file is substantially different from the file that this 
	   copyright, list of conditions, and disclaimer were originally attached to.
	::END NOTE::

    Copyright (c) 2013-2015, Rob Schmuecker
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

    * The name Rob Schmuecker may not be used to endorse or promote products
    derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED. IN NO EVENT SHALL MICHAEL BOSTOCK BE LIABLE FOR ANY DIRECT,
    INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
    BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
    OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
    EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

var dtree = function () {
	"use strict";
	
	var DTree = function() {
		var showDep = false,
			showPos = false,
			showGloss = false,
			dragEnabled = true;

		var levelHeight = 70, // 70 for instructions
			nodeSize = 6, // 6 
			ygap = 8, // gap between node and node text
			samelevelbuffer = 5, // horizontal gap between nodes bounding boxes at the same depth
			minxsep = 20, // minimum horizontal separation between node positions
			duration = 400, // transition duration in ms (400ms)
			orientation = "ltr", // (ltr) left-to-right; (rtl) right-to-left
			nodeHandleRadius = 23, // size of clickable node handle
			// Drop zone
			dropZoneRadius = 40; // size of drop zone
			
		// TODO: Add support for multiple callbacks eventually
		var endOfUpdateCallback, 
			depClickCallback,
			posClickCallback,
			nodeDragCallback,
			endDragCallback,
			nodeMouseoverCallback,
			nodeMouseoutCallback,
			nodeClickCallback,
			zoomCallback = function() {};
		
		// Nodes should be given class 'dtreeNode'; will be scaled up to nodeSize
		var nodeCreator = function(selection) {
			// Custom
			/* selection.append(function(d) {return this.appendChild(document.createElementNS("http://www.w3.org/2000/svg", d.data.shapeType));})
		       			    .attr("class", function(d) {return d.data.shapeClass + " dtreeNode";})
		       			    .attrs(function(d){ return d.data.shapeAttrs;})
		       			    // will scale up to full size during transition
		       			    .attr("transform", "scale(" + 0 +  "," + 0 + ")")
		       			    .style("vector-effect", "non-scaling-stroke");*/
			
			// add root node shape
		    /*selection.filter(function(d) {return d.data.oid == 0;}).append("polygon")
				     .attr("class", "rootRect dtreeNode")
				     .attr("points", "0 -1, 1 0, 0 1, -1 0")
				     // will scale up to full size during transition
				     .attr("transform", "scale(" + 0 + "," + 0 + ")")
				     .style("vector-effect", "non-scaling-stroke");*/
			
			// add root node shape
		    /*selection.filter(function(d) {return d.data.oid == 0;}).append("polygon")
				     .attr("class", "rootRect dtreeNode")
				     .attr("points", "0 -1, 1 0, 0 1, -1 0")
				     // will scale up to full size during transition
				     .attr("transform", "scale("+0+","+0+")");
		    // add non-root shape
		    selection.filter(function(d) {return d.data.oid != 0;}).append("circle")
			       .attr("class", "nodeCircle dtreeNode")
			       .attr("r", 1)
			       // will scale up to full size during transition
		       	   .attr("transform", "scale(" + 0 +  "," + 0 + ")");*/
			

			/* Firefox Bug on entrance animation of root polygon/circle observed in Firefox 61.0.1; 
			 * cause unknown; using just a fixed rectangle as a workaround */
			
			// add diamond for the root node
			selection.filter(function(d) {return d.data.oid == 0;}).append("rect")
				.attr("class", "rootRect")
				.attr("width", nodeSize*2/1.42) 
				.attr("height", nodeSize*2/1.42)
				// rotate it into a diamond
				.attr("transform", "rotate(-45) translate(-" + (nodeSize*1/1.42) + ",-" + (nodeSize*1/1.42) + ")");
			// add circles to all nodes except the root node			
			selection.filter(function(d) {return d.data.oid != 0;}).append("circle")
		       .attr("class", "nodeCircle dtreeNode")
		       .attr("r", 1)
		       // will scale up to full size during transition
	       	   .attr("transform", "scale(" + 0 +  "," + 0 + ")");
		};
		
		var zoomListener;
	
		// Related to dragging
		var domNode;
		var dragStarted = false, dragActive = false;
		var dropzoneNode = null, draggingNode = null;
	
		// D3 Selections of high-level groups
		var fullTree, egroup, ngroup, tegroup;
		
		var dragBehavior;
		var d3treelayout;
		var root;
		var svg;
	
		var updateTree, generateSvg;
		
		this.getNodeCreator = function() {
			return nodeCreator;
		};
		this.setNodeCreator = function(func) {
			nodeCreator = func;
			return this;
		}
		
		this.isCloneMode = function() {
			return cloneMode;
		};

		this.isMergeMode = function() {
			return mergeMode;
		};

		this.isDraggingNode = function(n) {
			return n === draggingNode;
		};
		this.isDragActive = function() {
			return dragActive;
		};

		this.getRoot = function() {
			return root;
		};
		this.getDropzoneNode = function() {
			return dropzoneNode;
		};

		this.getNodes = function() {
			return root.descendants();
		}
		
		this.setOrientation = function(o) {
			if(!(o === "ltr" || o === "rtl")) throw "Invalid direction";
			orientation = o;
			return this;
		};
		this.isDragEnabled = function() {
			return dragEnabled;
		};
		this.setDragEnabled = function(de) {
			svg.classed("dtree-interactive", de);
			dragEnabled = de;
			return this;
		};
		
		this.setShowDep = function(b) {
			showDep = b;
			return this;
		};
		this.isShowDep = function() {
			return showDep;
		};
		this.setShowPos = function(b) {
			showPos = b;
			return this;
		};
		this.isShowPos = function() {
			return showPos;
		};
		this.setShowGloss = function(b) {
			showGloss = b;
			return this;
		};
		this.isShowGloss = function() {
			return showGloss;
		};
		
		this.setEndDragCallback = function(c) {
			endDragCallback = c;
			return this;
		};
		this.setNodeMouseoutCallback = function(c) {
			nodeMouseoutCallback = c;
			return this;
		};
		this.setNodeMouseoverCallback = function(c) {
			nodeMouseoverCallback = c;
			return this;
		};
		this.setNodeDragCallback = function(c) {
			nodeDragCallback = c;
			return this;
		};
		this.setEndOfUpdateCallback = function(c) {
			endOfUpdateCallback = c;
			return this;
		};
		this.setDepClickCallback = function(c) {
			depClickCallback = c;
			return this;
		};
		this.setPosClickCallback = function(c) {
			posClickCallback = c;
			return this;
		};
		this.setNodeClickCallback = function(c) {
			nodeClickCallback = c;
			return this;
		}
		this.setZoomCallback = function(c) {
			zoomCallback = c;
			return this;
		}
		
		this.getSameLevelBuffer = function(){
			return samelevelbuffer;
		};
		this.setSameLevelBuffer = function(x){
			samelevelbuffer = x;
			return this;
		};
		
		this.getMinXSep = function(){
			return minxsep;
		};
		this.setMinXSep = function(x){
			minxsep = x;
			return this;
		};
		
		
		this.getLevelHeight = function(){
			return levelHeight;
		};
		this.setLevelHeight = function(h){
			levelHeight = h;
			return this;
		};
		
		this.getNodeSize = function() {
			return nodeSize;
		};
		this.setNodeSize = function(s) {
			nodeSize = s;
			return this;
		};
		
		this.getAnimationDuration = function() {
			return duration;
		};
		this.setAnimationDuration = function(d) {
			duration = d;
			return this;
		};
		
		this.setTransform = function(t) {
			svg.transition().duration(700).call(zoomListener.transform, t);
			return this;
		};
		
		this.resetView = function(newRootX) {
			// adjust the X value as necessary (e.g., due to browser window size adjustments)
			// NOTE: unclear whether animSource really matters in this case
			var animSource = {x0: root.x0,
							  y0: root.y0};
			root.x = newRootX;
			root.x0 = newRootX;
			updateTree(animSource);
			
			// Restore identity transform
			this.setTransform(d3.zoomIdentity);
			
			return this;
		};
	
		// Temporary edge between dragged node and potential drop target node
		var updateTempConnector = function(source, target) {
			var data = [];
			if (source !== null && target !== null) {
				data = [{lx: source.x0, ly: source.y0},
				        {lx: target.x0, ly: target.y0}];
			}
			var tEdgeJoin = tegroup.selectAll(".dropEdge").data(data);

			var lFunc = lineFunc(data);
			tEdgeJoin.enter().append("path")
            	.attr("class", "dropEdge")
            	.attr("d", lFunc)
            	.attr("pointer-events", "none");
			// TODO: Examine this; if nothing else, deserves a comment
			tEdgeJoin.attr("d", lFunc);

			tEdgeJoin.exit().remove();
		};
	
		// declares the function for drawing the edges
		var lineFunc = d3.line()
						 .x(function (d) { return d.lx; })
						 .y(function (d) { return d.ly; });
	
		// main tree update function
		updateTree = function(animSource) {
			var inverse = (orientation == "rtl" ? 1 : -1);
	
			var d3treeRootNode = root;
			
			var nodes = d3treeRootNode.descendants();
			var links = d3treeRootNode.links();

			// Node data join
			var nJoin = ngroup.selectAll(".node").data(nodes, function(d) { return d.data.oid; });

			// Enter the nodes.
			var nEnterG = nJoin
				.enter()
				.append("g")
				.attr("class", "node")
				// animSource.{x0,y0} is where the nodes originate from; y0+nodeSize is nice for descendants
				.attr("transform", function(d) {return "translate(" + animSource.x0 + "," + (animSource.y0+(d.data.oid==0?0:nodeSize)) + ")"; })
				.on("mouseover", nodeMouseoverCallback)
				.on("mouseout", nodeMouseoutCallback)
				.on("click", nodeClickCallback);
			
			if(dragEnabled) {
				// Add drag behavior to all entering nodes
				nEnterG.call(dragBehavior);
			}
			else {
				// Remove drag behavior from all nodes
				nJoin.on(".drag", null);
			}
		
			var bgXFunc = function(d) {
				return inverse < 0 ? -nodeSize : nodeSize-this.getBBox().width;
			};
			// Locate just under background box for the previously displayed value (i.e., the word form, the dependency, the gloss ...) 
			var bgYFunc = function(d) {
				var bbox = this.parentElement.previousElementSibling.getBBox();
				return bbox.y+bbox.height;
			};
			// Height, width of background boxes should equal height, width of the associated text content
			var bgWFunc = function(d) {
				return this.nextElementSibling.getBBox().width;
			};
			var bgHFunc = function(d) {
				return this.nextElementSibling.getBBox().height
			};
			// Sets y coordinate of <text> element (based on location of its background box)
			var textYFunc = function(d) { 
				var bbox = this.previousElementSibling.getBBox();
				return bbox.y+bbox.height;
			};
			
			function addTextToNodes(nodeG, klass, textF, yFunc, clickListener) {
				var labelG = nodeG.append("g");
				var labelBox = labelG.append("rect")
								 .attr("class", "tbx")
								 .attr("rx", 1)
								 .attr("ry", 1)
								 .style("fill-opacity", 0);
				var labelText = labelG.append("text")
							      .attr("class", klass)
							      .text(textF)
							      .attr("x", bgXFunc)
							      // the best dy value appears to be browser dependent; no lower than -.36 for Chromium
							      .attr("dy", "-.34em")
							      .style("fill-opacity", 0);
				// Set location of background box (calculated in part based upon text bounding box)
				labelBox.attr("width", bgWFunc) 
					  .attr("height", bgHFunc)
					  .attr("x", bgXFunc)
					  .attr("y", yFunc);
				// Set y location of depText (calculated in part based upon background bounding box)
				labelText.attr("y", textYFunc);
				
				labelText.on("click", clickListener);
			};
			addTextToNodes(nEnterG, 
				 "wform", 
				 function(d) { return d.data.text; }, 
				 function(d) { return ygap+nodeSize; }, 
				 null);
			if(showDep) {
				addTextToNodes(nEnterG, "dep", function(d) {return d.data.dep;}, bgYFunc, depClickCallback);
				// Update x in case of changes
				nJoin.selectAll(".dep").attr("x", bgXFunc);
			}
			if(showPos) {
				addTextToNodes(nEnterG, "pos", function(d) {return d.data.pos;}, bgYFunc, posClickCallback);
				// Update x in case of changes
				nJoin.selectAll(".pos").attr("x", bgXFunc);
			}
			if(showGloss) {
				addTextToNodes(nEnterG, "gloss", function(d) {return d.data.gloss;}, bgYFunc, null);
				// Update x in case of changes
				nJoin.selectAll(".gloss").attr("x", bgXFunc);
			}
			// Update width, x in case of changes
			nJoin.selectAll(".tbx").attr("width", bgWFunc).attr("x", bgXFunc);

			
			// Create the nodes
			nodeCreator(nEnterG);

			// Create transparent circles to act as handles for moving nodes so one doesn't
			// need to click exactly on the yellow circle or the text
			nEnterG.append("circle")
				   .attr("class", "nhandle")
				   .attr("r", nodeHandleRadius);

			// add "drop zone" circle to node
			nEnterG.append("circle")
				   .attr("class", "dropZone")
				   .attr("r", dropZoneRadius)
				   .style("display", "none")
				   .on("mouseover", function(node) {
					   if(node != draggingNode) {
						   // Display drop zone and temporary connector
						   dropzoneNode = node;
						   d3.select(node.figure).select(".dropZone").classed('dropZone-highlight',true);
						   updateTempConnector(dropzoneNode, draggingNode);
					   }
				   })
				   .on("mouseout", function(node) {
					   // Hide drop zone and temporary connector on mouse out
					   d3.select(node.figure).select(".dropZone").classed('dropZone-highlight',false);
					   dropzoneNode = null;
					   updateTempConnector(null, null);
				   });

			var nMerge = nEnterG.merge(nJoin);
		
			// Perform tree layout
			nMerge.each(function (d) {
				var bbox = this.getBBox();
				d.figure = this;
				// NOT IDEAL: essentially, the code below fixes the height and width. Thus,
				// if there are substantive changes, d.width and d.height must be deleted
				//
				// This is being done to prevent small shifting of nodes due to 'bolding' the font
				// on hover causing temporary bounding box size changes that are affecting the layout
				if(!d.width) {
					d.width = bbox.width;
				}
				if(!d.height) {
					d.height = bbox.height;
				}
			});

			// Determine depth and x coordinates for nodes
			dtreeLayout(root, inverse, function(a, b){return a.data.oid-b.data.oid;});
			// Set y coordinates of nodes
			nMerge.each(function(d) {d.y = d.depth * levelHeight;});
			// Revise node coordinates to prevent shifting of the root node
			// Save off copies of root.x and root.y (root.x and root.y will be updated in the upcoming forEach call)
			var rootX = root.x;
			var rootY = root.y;
			nMerge.each(function(d) {d.y = d.y+(root.y0-rootY);
						  d.x = d.x+(root.x0-rootX);});
		
			// Data join for edges
			var eJoin = egroup.selectAll(".depEdge")
				.data(links, function(d) { return d.target.data.oid; });

			// create a group to hold path graphics and path labels
			var eEnterG = eJoin.enter().append("g").attr("class", "depEdge");

			// add path graphics to grow from bottom of animation source node
			eEnterG.append("path")
				.attr("d", function(d) {
					var points = [
					              {lx: animSource.x0, ly: animSource.y0+(animSource==d.target?-nodeSize:nodeSize)},
					              {lx: animSource.x0, ly: animSource.y0+(animSource==d.target?-nodeSize:nodeSize)}
					              ];
					return lineFunc(points);
				});

			// edgeAnimation
			var eUpdate = eJoin.merge(eEnterG)
							  // disable events on transitioning elements
							  .attr("pointer-events", "none")
							  .transition()
							  .duration(duration)
							  // restore events at end of transition
							  .on("end", function() { d3.select(this).attr("pointer-events", null); });
			// set final shape of the path
			eUpdate.select("path")
				   .attr("d", function(d) {
                      var points = [
                                    {lx: d.source.x, ly: d.source.y+nodeSize},
                                    {lx: d.target.x, ly: d.target.y}
                      ];
                      return lineFunc(points);
				   });

			// remove unnecessary edges
			var eExit = eJoin.exit().remove();
			// for fading out exiting edge labels (not currently applicable)
			// eExit.select("text").style("fill-opacity", 0); 

			// ~~~Node animation~~~
			var nUpdate = nMerge
				// disable mouse events on transitioning elements
				.attr("pointer-events", "none") 
				.transition()
				.duration(duration)
				.attr("transform", function(d) {
					return "translate(" + d.x + "," + d.y + ")";
				})
				// restore mouse events on completion
				.on("end", function() { d3.select(this).attr("pointer-events", null); });
		
			nUpdate.selectAll("text").style("fill-opacity", 1);
			nUpdate.selectAll(".tbx").style("fill-opacity", .9);
			
			// Scale up nodes to proper size
			nUpdate.selectAll(".dtreeNode").attr("transform", "scale(" + nodeSize +  "," + nodeSize + ")");
			// ~~~END ENTERING NODES ANIMATION~~~

			// ~~~Exiting nodes animation~~~
			// exiting nodes translate to animation source (x,y)
			var nExit = nJoin.exit()
				             .attr("pointer-events", "none") // disable mouse events on transitioning elements
				             .transition()
				             .duration(duration)
				             .attr("transform", function(d) {
					            return "translate(" + animSource.x + "," + animSource.y+nodeSize + ")";
				             })
				             .remove();
		
			// Exiting nodes shrink (not currently applicable)
			// nExit.selectAll(".dtreeNode").attr("transform", "scale(" + 0 +  "," + 0 + ")")
			// Fade out exiting node labels (not currently applicable)
			//nExit.selectAll("text").style("fill-opacity", 0);
			//nExit.selectAll(".tbx").style("fill-opacity", 0);
			// ~~~End exiting animation~~~

			// Keep copy of resulting node positions
			nodes.forEach(function(d) {
				d.x0 = d.x,
				d.y0 = d.y;
			});
		
			if(endOfUpdateCallback) endOfUpdateCallback();
		}; // end updateTree(...)
	
		generateSvg = function(d3TreeRoot, 
								 svgDOMnode,
								 layoutWidth,
								 layoutHeight,
								 rootX,
								 rootY,
								 treedir) {
			try {	
				svg = d3.select(svgDOMnode);
				svg.classed("dtree-interactive", dragEnabled);
				
				// Remove any existing children (reset the svg element)
				while (svgDOMnode.firstChild) {
					svgDOMnode.removeChild(svgDOMnode.firstChild);
				}
				// Remove the current transform if it exists
				if(zoomListener != null) {
					svg.call(zoomListener.transform, d3.zoomIdentity);
					zoomListener.on("zoom", null);
					//setTransform(d3.zoomIdentity);
				}
				
				d3treelayout = d3.tree().size([layoutWidth, layoutHeight]);

				// define the zoomListener which handles "zoom" event constrained within the scaleExtents
				zoomListener = d3
    					     .zoom()
    					     .scaleExtent([0.5, 2])
    					     .on("zoom", function () {
    					    	fullTree.attr("transform", 
  					    			"translate(" + d3.event.transform.x + "," + d3.event.transform.y + ")scale(" + d3.event.transform.k + ")");
    					    	zoomCallback();
    					     });
				svg.call(zoomListener).on("dblclick.zoom", null); //disable zoom on double click

				// Force background to white
				// TODO: need to set height, width for this to take effect....
				svg.append("rect").attr("class", "backRect");

				// creates a group for holding the whole tree, which allows "zoom" to function
				fullTree = svg.append("g");
				// If one scales it, one also needs to translate it...
				//var myScale = 2;
				//zoomListener.translateBy(svg, 0, (rootY*myScale));
				//zoomListener.scaleTo(svg, 2);
				
				// create a group for holding the edges -- this group will be drawn first
				egroup = fullTree.append("g");
				// create a group for holding the nodes -- this group will be drawn second
				ngroup = fullTree.append("g");
				// create a group for holding the temporary edges(s) -- this group will be drawn last
				tegroup = fullTree.append("g");

				// define the root and its initial location
				root = d3TreeRoot;
				root.x0 = rootX;
				root.x = rootX;
				root.y0 = rootY;
				root.y = rootY;

				// prepare node for dragging by removing children and paths
				function initiateDrag(d, domNode) {
					draggingNode = d;

					// Ignore pointer events on own drop zone
					d3.select(domNode).select('.dropZone').attr('pointer-events', 'none').classed('dropZone-dragging', true);
					// Show all drop zones
					d3.selectAll('.dropZone').style('display', 'block');
					// Send dragged node to the back so that the mouseover method will fire properly when we're over the other nodes
					ngroup.selectAll(".node").sort(function(a, b) { 
						if (a.data.oid !== draggingNode.data.oid) return 1;
						else return -1;
					});

					var descendants = draggingNode.descendants();
					// if nodes has children, remove the links and nodes
					if (descendants.length > 1) {
						var links = draggingNode.links(); 
						egroup.selectAll(".depEdge")
							.data(links, function (d) { return d.target.data.oid; })
							.remove();
						ngroup.selectAll(".node")
							.data(descendants, function(d) { return d.data.oid; })
							// NOTE: descendants() includes the node it is called on
							.filter(function(d) { return d.data.oid !== draggingNode.data.oid; })
							.remove();
					};
					// 	remove parent edge
					egroup.selectAll(".depEdge")
		    						.filter(function(d) { return d.target.data.oid === draggingNode.data.oid; })
		    						.remove();
					dragStarted = false;
					dragActive = true;
				};

				// Drag-and-drop
				dragBehavior = d3.drag()
				.on("start", function(d) {
					if (d === root) {return;} // root may not be moved
					dragStarted = true;
					d3.event.sourceEvent.stopPropagation();
					// it's important that we suppress the mouseover event on the node being dragged. Otherwise it will absorb the mouseover event and the underlying node will not detect it d3.select(this).attr('pointer-events', 'none');
				})
				.on("drag", function(d) {
					if (d === root) {return;} // root may not be moved
					if (dragStarted) {
						domNode = this;
						initiateDrag(d, domNode);
					}

					if(nodeDragCallback) nodeDragCallback(d);
					
					d.y0 += d3.event.dy;
					d.x0 += d3.event.dx;
					var node = d3.select(this);
					node.attr("transform", "translate(" + d.x0 + "," + d.y0 + ")");
					updateTempConnector(dropzoneNode, draggingNode);
				})
				.on("end", function(d) {
					if (d === root) {return;} // root may not be moved
					domNode = this;
					if (dropzoneNode) {
						// now remove the element from the parent, and insert it into the new elements children
						var index = draggingNode.parent.children.indexOf(draggingNode);
						if (index > -1) {
							// remove the element
							draggingNode.parent.children.splice(index, 1);
							if(draggingNode.parent.children.length==0) {
								delete draggingNode.parent.children;
							}
						}
						if (dropzoneNode.children !== undefined) {
							dropzoneNode.children.push(draggingNode);
							// sort nodes to maintain original word order!
							dropzoneNode.children = dropzoneNode.children.sort(function(a, b) {
								return a.oid - b.oid;
							});
						}
						else {
							dropzoneNode.children = [];
							dropzoneNode.children.push(draggingNode);
						}
						draggingNode.parent = dropzoneNode;
					}     
					endDrag(d);
				});

				// when node is dropped, update tree
				function endDrag(d) {
					// Reset drop zones to normal (hidden) state
					d3.selectAll('.dropZone').style('display', 'none').classed("dropZone-dragging", false)
					  .classed("dropZone-highlighted", false);
					d3.select(domNode)
					  .select('.dropZone')
					  .attr('pointer-events', null); // restores pointer-events
					
					if(endDragCallback) endDragCallback(d);
					
					if(dropzoneNode != null) {
						updateTempConnector(null, null);
						// animations should go to/from the drop target
						updateTree(dropzoneNode);
					}
					else if (draggingNode !== null) {
						updateTempConnector(null, null);
						// the dragged node is moving back to where it started
						// let the animations come from the dragged node
						updateTree(draggingNode);
					}
					draggingNode = null;
					dropzoneNode = null;
					dragActive = false;
				};

				// lay out the initial tree
				updateTree(root);
    
			}
			catch(ex) {
				alert("generateSVG: " + ex);
			}
		};// end generateSVG(...)
		
		function dtreeLayout(root, invert, nodeComparator) {
			root.depth = 0;
			var nodesByDepth = [];
			xrdfs(null, root, 0, nodesByDepth, nodeComparator);
			shiftXValues(root, nodesByDepth, samelevelbuffer, invert, nodeComparator);
		} // end dtreeLayout(...)


		function shiftXValues(root, nodesByDepth, samelevelbuffer, invert, nodeComparator) {
			var maxDepth = nodesByDepth.length;
		    var allNodes = [];
		    for(var i = 0; i < maxDepth; i++) {
		    	var nodesAtDepth = nodesByDepth[i];
		    	nodesAtDepth.sort(nodeComparator);
		    	var numAtDepth = nodesAtDepth.length;
		    	for(var j = 0; j < numAtDepth; j++) {
		    		allNodes.push(nodesAtDepth[j]);
		    	}
		    }
		    allNodes.sort(nodeComparator);
		    
		    var changesMade = true;
		    while(changesMade) {
		    	changesMade = false;
		    	var numLevels = nodesByDepth.length;
		    	for(var i = 0; i < numLevels; i++) {
		    		var nodesAtDepth = nodesByDepth[i];
		    		var numNodes = nodesAtDepth.length;
		    		var minX = 0;
		    		for(var j = 0; j < numNodes; j++) {
		    			var node = nodesAtDepth[j];
		    			if(invert < 0) {
		    				if(node.x < minX) {
		    					changesMade = true;
		    					node.x = minX;
		    				}
		    			}
		    			else {
		    				if(node.x > minX) {
		    					changesMade = true;
		    					node.x = minX;
		    				}
		    			}
		    			var bbWidth = node.width;
		    			minX = node.x - invert * (bbWidth + samelevelbuffer); 
		    		}
		    	}
		    	
		    	var minX = 0;
		    	var numNodes = allNodes.length;
		    	for(var i = 0; i < numNodes; i++) {
		    		var node = allNodes[i];
		    		if(invert < 0) {
		    			if(node.x < minX) {
		    				changesMade = true;
		    				node.x = minX;
		    			}
		    		}
		    		else {
		    			if(node.x > minX) {
		    				changesMade = true;
		    				node.x = minX;
		    			}
		    		}
		    		minX = node.x - invert * minxsep;
		    	}
		    }
		} // end shiftXValues(...)

		// TODO: rename this thing
		function xrdfs(parent,node,depth,nodesByDepth,nodeComparator) {
		    node.x = -1;
		    node.y = -1;
		    node.depth = depth;
		    if(nodesByDepth.length <= depth) nodesByDepth.push([]);    
		    var nodesAtDepth = nodesByDepth[depth];
		    
		    nodesAtDepth.push(node);
		    		
		    if(node.children) {
		    	var numChildren = node.children.length;
		    	var childrenCopy = node.children.slice(0);
		    	childrenCopy.sort(nodeComparator);
		    	for(var e = numChildren-1; e >= 0; e--) {
		    		var daughter = childrenCopy[e];
		    		xrdfs(node,daughter,depth+1,nodesByDepth,nodeComparator);
		    	}
		    }
		} // End xrdfs(...)
		
		this.updateTree = updateTree;
		this.generateSvg = generateSvg;
	
	};// end DTree()

	// dtree module
	return {
		DTree: DTree
	};
}(); // end of module
