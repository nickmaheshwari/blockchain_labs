Lab 7 Documentation:			author: Nicholas Maheshwari
Lab instructions: https://www.classes.cs.uchicago.edu/archive/2021/summer/56600-1/LABS/LAB.7/lab_7.html
-----------------------------------------------------------------------
Dependencies: Guava (see: https://mvnrepository.com/artifact/com.google.guava/guava/30.1.1-jre)  
 Simply copy the following code into your pom.xml within the 'dependencies' tags, and then run 'mvn clean install' from the project's directory:
 
 <dependency>
			<groupId>com.google.guava</groupId>
			<artifactId>guava</artifactId>
			<version>30.1.1-jre</version>
 </dependency>

To run from command line: 
	-Add following code to pom.xml within the plugins tags, replace 'blockchain_lab5' within the <mainClass> tags with your project's folder name
			<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>exec-maven-plugin</artifactId>
				<version>1.2.1</version>
				<configuration>
					<mainClass>blockchain_lab7.BitcoinAddressGenerator</mainClass>
				</configuration>
			</plugin>
			
	-Within the program directory, execute the following command:  mvn exec:java
	
Running from IDE:
	-Ensure all dependencies are properly installed (see above). 
	-Run either by hitting the 'Run' button on the tool bar or using the 'Run' options within the navigation bar
	