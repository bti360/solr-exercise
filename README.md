# solr-exercise
Solr exercise meant to develop Lucene text analysis knowledge + lightly touching on Solr update processors.

## Get started...
1. Clone this git repository onto your local workstation
2. `cd` into the project directory
3. View the available gradle tasks via `./gradlew` for Mac/Linux or `gradlew.bat` for windows
4. Setup eclipse via `./gradlew eclipse` or IntelliJ via `./gradlew idea`
  * Those too cool for IDEs can skip to step 6 and run tests via `./gradlew test`
5. Import the project into your IDE
6. Run the tests and see what breaks!
  * Use your new found knowledge of text analysis to solve the majority of the test fails.
    * The Solr schema is where you will find the field definitions along with the associated analysis chains found at `src/test/resources/solr-conf/gt/conf/schema.xml`
  * There is one challenge that will involve creating a custom update processor to combine the `latitude` and `longitude` fields into a final `lat-lon` field in the format of `<latitude value>,<longitude value>`
    * The update processor is defined in the SolrConfig file located at `src/test/resources/solr-conf/gt/conf/solrconfig.xml`. For an example custom processor see an example written in either Javascript (`src/test/resources/solr-conf/gt/conf/base64-decoder.js`) or Java (`src/main/java/..../Base64DecodingUpdateProcessorFactory.java`)

## Once you are happy with the test runs, let's index some real data!
1. If you created a custom Java update processor run the `./gradlew jar` task.
2. Unzip the Solr directory
3. Open a new command prompt and `cd` into the Solr directory
4. Start Solr via `bin/solr -f -s <path-to-solr-exercice-workspace-dir>/src/test/resources/solr-conf/`
5. Download the sample data via `./gradlew downloadExampleData` task in the exercise directory
6. Copy and run the url that was emitted from the download task
7. If all went well, you indexed the data! You can access the admin page at `http://localhost:8983/solr/#/gt`