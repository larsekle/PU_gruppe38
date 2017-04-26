# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* The repository contains source code for BuddyBOT - a software engineering project. 
* This repository contains source code for users in /src where /VisibleForUser is the only file users interact with. 
* This repository contains source code for the BuddyBOT web page in /BuddyBOT web page folder. The Bootstrap we have used is separated from the other web page files in different sub-folders.
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

Please read this document to set up BuddyBOT for your programming course.

1. Set up the system already implemented for TDT4100 at NTNU: For the course TDT4100 there are only a few steps to set up BuddyBOT as a runnable software. First, decide whether to use the mysql.ntnu server or an external server. We recommend using an external server for students to use the system without being connected to the NTNU VPN. Modify JUnit-tests to include the diagnosing part added to example AccountTest and the Card-exercise in package VisibleForUser. Web page files are finished, and only the database-connect method need to be changed so that it connects to your database. Get access to a domain and deploy the web page. Deploy system to students by adding it to the Github repository student pull their exercise tests from.

2. Set up the system for a new programming course: To implement BuddyBOT for a new programming course, do the following: Set up a database according to the ER model and change connect-method in the java JDBC class so that it connects to your database. The database must support the SQL language. Structure your course according to the structure example for TDT4100, the outer layer will be a basis for the tags used to diagnose students. These must be added in the functional Unit-tests (recommend JUnit for Java courses). Use the Hashtag.sendToDB method to insert failures to your database from functional Unit-tests. If changing the tags, you should also change the Hasgtag.TAGS list to contain your new tags. This will be used to cross-check spelling errors before allowing tags to be inserted to the Failures table in your database. Web page files are finished, and only the database-connect method need to be changed so that it connects to your database. Get access to a domain and deploy the web page.
TDT4140 Spring 2017 Group 38 - BuddyBOT 48 | P a g e

3. Set up the system as a student: Install Eclipse if not already installed. Pull source code from Github repository provided from supervisor, and start programming! You will be asked to register some login credentials for the web page the first time BuddyBOT appears to help.

### Contribution guidelines ###

* Write test for the software core in /Tests. Every Java class has its own TestCase in JUnit. Edit or add test in the TestCase where you have contributed.

### Who do I talk to? ###

* Lars Erik Kleiven, Ingrid E. Hermanrud, Sigrid Lea Fosen, Helena Pontseele.
* Pekka Abrahamsson at the Norwegian University of Science and Technology.