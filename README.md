# Build and usage instructions

## Build instructions

### Modify source code with your user credentials and project information

**IMPORTANT:** this will be configurable via property file in the future

Modify all instances of
**replace_me_[XXX]**
in the file
**src/main/java/com/bittium/qrapids/issuetracker/rest/IssueEndpoints.java**

### How to build

This is a normal Maven project. You can use whatever IDE that supports Maven
projects to build this. Maybe the easiest way to build this is building
from the command line using command **mvnw package**, which comes with these
source codes. It is a Maven wrapper to handle building if you don't have
Maven installed natively on your machine. Of course you can build with native
Maven also using the command **mvn package**.

    #### EXAMPLE: using Maven wrapper
    cd qr-issuetracker
    mvnw package

After the build is done the output JAR file can be found from the directory **target**.

## User instructions (you need to read and do 'build instructions' also)

### Example usage of the REST API endpoint after the module is running

    #### REQUEST
    curl -XPOST <qr-issuetracker server>:8080/issue/create -H 'Content-Type: application/json' -d '
    {
        "issue_summary": "QRAPIDS: This is a test issue",
        "issue_description": "Lorem ipsum",
        "issue_type": "Story"
    }'

    #### RESPONSE
    {
        "issue_url": "https://<your jira url>/rest/api/latest/issue/201860",
        "issue_id": "RPA-425"
    }
