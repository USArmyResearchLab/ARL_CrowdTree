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

/* used by guidelines.html */
var GuidelinesModule = (function () {
	function init() {
		var help_div = $("#div_help");
		var guide_div = $("#guidecontents");
		var toc = $("#toc");
		$("#newwindow").click(
			function() {
				var html;
				// get current style info
				var style = guide_div.attr("style");

				// if table of contents is closed, open before
				// copying to window?
				if (!(toc.is(":visible"))) {
					toc.show();
					// remove custom size
					guide_div.removeAttr("style");
					html = $(".guidemain").html();
					// close again
					toc.hide();
				} else {
					guide_div.removeAttr("style");
					html = $(".guidemain").html();
				}
				// restore custom size
				guide_div.attr("style", style);
				help_div.hide();
				var cssdir = window.location.origin	+ '/arlcrowdtree/css/';
				var w = window.open();
				$(w.document.head)
						.html('<meta charset="UTF-8"/><title>Annotation Guidelines</title><link rel="stylesheet" type="text/css" href="'
										+ cssdir
										+ 'standalone_guidelines.css"><link rel="stylesheet" type="text/css" href="'
										+ cssdir
										+ 'bootstrap.min.css"><link rel="stylesheet" type="text/css" href="'
										+ cssdir
										+ 'dtree_svg.css">');
				$(w.document.body).html(html+"</html>");
			});
	
		// animation
		$("#nav").click(
			function() {
				$("#tabs li").toggle();
				$.when(toc.animate({
					width : 'toggle'
				}, "fast")).done(
						function() {
							guide_div.width(guide_div.width() + (toc.is(":visible")?-300:300));
						});

			});
	
		var tab1 = $("#tab1");
		var tab2 = $("#tab2");
		tab1.click(function() {
			tab1.attr("class", "active");
			tab2.attr("class", "");
		});

		tab2.click(function() {
			tab2.attr("class", "active");
			tab1.attr("class", "");
		});
	
		help_div.draggable({
			handle : ".modal-header",
			containment : "body"
		})
		.resizable(
			{
			alsoResize : "#guidecontents",
			minWidth : 485,
			minHeight : 485,
			//maxHeight : isMTurkMode()?877:undefined,
			containment : "body"
			});

		$("#btn_guide_x").click(function() {help_div.toggle();});
		
		var examples = [];
		var exampleTables = $(".extable");
		exampleTables.each(function(index) {
					var text = $(this).find(".extext").text();
					examples.push({
						value: "#"+this.id,
						label: text
					});
				});
		
		var ex = $("#examples");
		for (var i = 0; i < examples.length; i++) {
			var a = $(document.createElement("a"));
			a.addClass("list-group-item")
			 .attr("href", examples[i].value)
			 .html(examples[i].label);
			ex.append(a);
		}

		var searchField = $("#search");
		searchField.autocomplete({
			source: examples,
			select: function(event, ui) {
				event.preventDefault();
				searchField.val(ui.item.label);
				window.location.href = ui.item.value;
			},
			focus: function(event, ui) {
				event.preventDefault();
				searchField.val(ui.item.label);
			}
		});

		searchField.keypress(function(event) {
			if(event.keyCode == 13) {
		    	if(!($("#input").is(":focus"))) {
		    		event.preventDefault();
		        	event.stopPropagation();
		    	}
			}
		});
	};
	
	return {init: init};
})();

GuidelinesModule.init();
