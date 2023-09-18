#Compiles and runs tests for AE

runAlgorithmEngineerTests: compileAETests
	java -jar junit5.jar -cp . --select-class=AlgorithmEngineerTests
	make clean

compileAETests: AirportAE.java AirportInterface.java AlgorithmEngineerTests.java FlightAE.java FlightInterface.java Graph.java GraphADT.java
	javac -cp .:junit5.jar AlgorithmEngineerTests.java

#Compiles and runs tests for BD

runBackendDeveloperTests: BackendDeveloperTests.class
	java -jar junit5.jar -cp . --select-class=BackendDeveloperTests

BackendDeveloperTests.class: BackendDeveloperTests.java BackendInterface.java FlightReaderBD.java FlightReaderInterface.java GraphBD.java GraphInterface.java GraphADT.java GraphADT.java
	javac -cp .:junit5.jar BackendDeveloperTests.java

#Compiles and runs tests for FrontendDeveloper

compileFDTests: AirplaneReaderFD.java Airport.java AirportBackendFD.java AirportFrontendFD.java AirportFrontendInterface.java FlightInterface.java BackendInterface.java BaseGraph.java
	javac -cp .:junit5.jar FrontendDeveloperTests.java

runFrontendDeveloperTests: compileFDTests
	java -jar junit5.jar -cp . --select-class=FrontendDeveloperTests

#Compiles and runs tests for DataWrangler

compileDWTests: Airport.java AirportInterface.java DwTest.java Flight.java FlightInterface.java FlightReader.java FlightReaderInterface.java
	javac -cp .:junit5.jar DwTest.java 

runDataWranglerTests: compileDWTests
	java -jar junit5.jar -cp . --select-class=DwTest

#Compiles and runs all of the tests
runTests: runDataWranglerTests runFrontendDeveloperTests runBackendDeveloperTests runAlgorithmEngineerTests

#Runs the app

compileTests: compileDWTests compileFDTests BackendDeveloperTests.class compileAETests

compileApp: Airport.java AirportFrontendFD.java AirportFrontendInterface.java AirportInterface.java BackendBD.java BackendInterface.java Flight.java FlightApp.java FlightInterface.java FlightReader.java FlightReaderInterface.java Graph.java GraphADT.java Path.java
	javac Airport.java
	javac AirportFrontendFD.java
	javac AirportFrontendInterface.java
	javac AirportInterface.java
	javac BackendBD.java
	javac BackendInterface.java
	javac Flight.java
	javac FlightApp.java
	javac FlightInterface.java
	javac FlightReader.java
	javac FlightReaderInterface.java
	javac Graph.java
	javac GraphADT.java
	javac Path.java
run: compileApp
	java FlightApp.java
	make clean

#Cleans all class files
clean:
	rm *.class
