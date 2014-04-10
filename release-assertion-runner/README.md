Release Assertion Runner
========================

This module runs release assertions on the files listed in the manifest file.

Manifest files live in the release-assertion-resources module. To find the currently configured manifest file search
the release-assertion-runner pom.xml for 'manifestFileName'.


Run assertions on maven artifacts
---------------------------------
To run assertions on the current release first build the whole release-assertion-toolkit project to prime your local
repository:
	mvn clean install
Then cd to the release-assertion-runner directory and do:
	mvn clean install -P run-file-qa

The report Excel file is generated under target/generated-sources/release-file-qa-report.xls .


Run assertions on local files
-----------------------------
To run assertions on local files first follow the steps for running assertions on maven artifacts. This will set up
the target directory with the previous and current release as defined under the properties section of the
release-assertion-runner pom.xml.

You may then change the files under generated-sources and run the assertions again using:
	mvn install -P run-file-qa
The report file will be regenerated
N.B. the 'clean' step is omitted in order to keep the changes in the target directory.

You may also add files to the manifest xml but be sure to check the pom to find which manifest filename is in use,
as previously mentioned.
