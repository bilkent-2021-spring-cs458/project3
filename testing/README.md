# Build Instructions

[Apache Maven](https://maven.apache.org) is used for this project's management. Hence,
all of our dependencies are automatically installed when our project is built with Maven. Many
modern IDE’s support Maven out-of-the-box. To build our project in such an IDE, the project
folder should be imported as a Maven project to the project’s workspace.

To build the project without using an IDE, Maven command-line tool should be installed to the
system. For instructions, see https://maven.apache.org/install.html. After installing Maven, our
project can be built by executing the following command in our project’s root folder:
`mvn install`

Executing this command will download all the dependencies and generate the jar file required
for running this project. Note that JRE 14 is required to run the jar file. The jar file will be
generated in the target folder. Then using the java -jar command, it can be executed (Note that the
version number in the jar’s name can be different):
`java -jar target/maps_testing-0.0.1.jar`

Please note that the environment where this command is executed should be configured for Selenium. Depending on the browser, specific WebDriver should be installed. The generated jar file also allows different WebDrivers to be used. Passing an argument while running will use the specific driver. Available options are opera, chrome, firefox. Chrome is used by default. Example:
`java -jar target/maps_testing-0.0.1.jar firefox`
