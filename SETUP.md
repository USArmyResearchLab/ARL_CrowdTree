## Setup

This document describes the steps required to use this software. Please review README.md prior to reading this document.


#### Step 1) Servlet container

A servlet container, such as Apache Tomcat, is required for this software to function properly. See http://tomcat.apache.org/ for more information on installing and running Apache Tomcat.

#### Step 2) Required dependencies
 
You will need to obtain copies of the following libraries in order to compile and use ARL CrowdTree. 

**Java dependencies**

To compile the Java code, you will need a Java JDK and copies of the following Java libraries. 

* jackson-core (available at http://central.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.9.6/jackson-core-2.9.6.jar)
* jackson-databind (available at  http://central.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.9.6/jackson-databind-2.9.6.jar)
* jackson-annotations (available at http://central.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.9.6/jackson-annotations-2.9.6.jar)
* Servlet API implementation such as the servlet-api.jar that comes with Apache Tomcat.


> Place the JAR files in the project's lib/ directory.

#### Step 3) Deploying ARL CrowdTree

To deploy ARL CrowdTree on Apache Tomcat, it must be bundled into a WAR file and placed in your Apache Tomcat installation's webapps/ subdirectory. The example provided in the examples/en\_annotation directory illustrates how to do this. Please see examples/en\_annotation/README.html for futher instructions.

#### Step 4) Accessing ARL CrowdTree

If you have deployed ARL CrowdTree correctly, it should be accessible at a URL such as the one below. (The URL below assumes that Apache Tomcat is running on your local machine (localhost) on port 8080.) 
 
http://localhost:8080/arlcrowdtree/tree\_editor\_app.html?tree=0

#### Using ARL CrowdTree with Amazon Mechanical Turk

It is possible to use ARL CrowdTree with Amazon Mechanical Turk (AMT) by using AMT's ExternalQuestion HITs (https://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_ExternalQuestionArticle.html). A detailed description of how to use AMT or the AMT API is beyond the scope of this document. 
