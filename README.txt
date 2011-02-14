Akka Training project
=====================

Each exercise is explained in "src/test/exercises"
The implementation-stub of each exercise is located in "src/main/scala/akka/training"

Tests to verify exercises are located in "src/test/scala/exercises/"
Solutions to the exercises are located in "src/test/scala/key/"

The labs are test-driven and uses ScalaTest WordSpec with MustMatchers. 

Akka Training Lab Setup Instructions
====================================

What you need
-------------

  - Scala 2.8.1
  - SBT 0.7.5.RC0
  - Latest version of IntelliJ IDEA 10.x
  - Scala Plugin for IntelliJ

Installation of tools
---------------------

  - Download and install Scala 2.8.1 from: http://www.scala-lang.org/downloads.
  - Add 'SCALA_HOME' environment variable to the root of the installation.
  - Add 'SCALA_HOME/bin' to you runtime 'PATH'.
  - Download and install SBT 0.7.0.RC0. Follow instructions on: http://code.google.com/p/simple-build-tool/wiki/Setup
  - Download IntelliJ IDEA 10 Community Edition from: http://www.jetbrains.com/idea/download/index.html 
  - Add Scala Plugin to IntelliJ. 
  - Go to Preferences -> Plugins -> Available (Tab). Find the Scala plugin and install it.
  - Open up the project in IntelliJ: File -> Open project -> navigate to the folder and open it

Installation of labs
--------------------

  - Download Akka Training distribution from: TBA
  - Copy and extract the akka-training.zip file 

Test the installation
---------------------

  - Open a shell (cmd prompt) and invoke 'scala' to see if you can run Scala.
  - Open a shell (cmd prompt) and invoke 'sbt' to see if you can run SBT.
  - Open a shell (cmd prompt) and go to the 'akka-training' folder. Run 'sbt test' which should compile and run the tests for the labs.
  - Open IntelliJ, import and build the akka-training project. (Build -> Make Project).
