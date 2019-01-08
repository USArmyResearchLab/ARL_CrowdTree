## README

### Project Name
ARL CrowdTree

### Version
1.0
 
### Purpose of Software
ARL CrowdTree is a graphical web-based annotation tool for editing dependency trees. It is designed to be used with Amazon's Mechanical Turk crowdsourcing platform but can be used outside Mechanical Turk as well. This software is likely of interest primarily to researchers in computational linguistics and natural language processing (NLP) who wish to run dependency tree annotation projects.

### Development
ARL CrowdTree was developed at the U.S. Army Research Laboratory by College Qualified Leaders (CQL) intern Nhien (Theresa) Phan and Dr. Stephen Tratz. It derives from components of the EasyTree software developed by CQL intern Alexa Little as well as components of code written by Dr. Tratz while in graduate school (https://www.isi.edu/publications/licensed-sw/fanseparser/index.html).  

CQL program URL: https://www.usaeop.com/program/cql/

Some of the visual design is inspired by the Tree Editor TrEd (https://ufal.mff.cuni.cz/tred/).

### Point of Contact
Please send questions and/or comments to Stephen Tratz <stephen.c.tratz.civ@mail.mil>. If you have interest in collaborating on a newer version of this software, please don't hesitate to reach out.

### License
ARL CrowdTree is licensed under the Apache 2.0 license. Please see LICENSE.txt for details.

### Disclaimer of warranty
Please see LICENSE.txt for details.

### Project structure

|File/directory    | Description    |
|------------------|:---------------|
|client/	   | #HTML, CSS, & JavaScript code for the graphical frontend |
|examples/     | #contains example(s) |
|lib/ 	       | #dependent Java libraries |
|src/	       | #Java code for the servlet |
|code.json	   | #See https://code.gov |
|LICENSE.txt   | #License text for this software |
|README.md 	   | #this file |

### Setup

Please see SETUP.md for more information on setting up and using this software.

### Citing this software

If you wish to cite this software, please cite the following conference paper. (It is worth noting that this released version of the software does not contain code for training a dependency parser while users are annotating. The parser training code may be included in a future release.) 

Stephen Tratz and Nhien Phan. 2018. A Web-based System for Crowd-in-the-Loop Dependency Treebanking. In the Proceedings of the Eleventh International Conference on Language Resources and Evaluation (LREC 2018). Miyazaki, Japan.
					
