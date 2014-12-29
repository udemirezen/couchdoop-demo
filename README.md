Couchdoop Demo
==============

This repository is a demo project for Couchdoop, a Hadoop connector for
Couchbase. It is part of the Couchdoop article published on Cloudera's blog.

We assume that we have a news website which uses Couchbase as its database and
we would like to use Hadoop to recommend articles to users. The website tracks
user activity during a session and stores it in a Couchbase document. All user
sessions documents need to be imported into HDFS where they are used as input
for the recommender system. The computed recommendations which are stored in
HDFS need to be exported to Couchbase where the website can use them.

The project contains two Hadoop MapReduce drivers:

1. [`com.avira.couchdoop.demo.ImportDriver`](/src/main/java/com/avira/couchdoop/demo/ImportDriver.java)
  - Imports JSON session documents from Couchbase, reformats them into a
	delimited text format and writes them in HDFS in a user specified path. It
	is assumed that the recommender expects text files where on each line there
	is the session ID followed by tab and a list of article identifiers
	separated by commas. Check ["data/sessions.json"](/data/sessions.json) data
	sample file to see how Couchbase session documents look like and
	["data/imported_sessions.txt"](/data/imported_sessions.txt) to see how the
	output of the Hadoop job triggered by this driver.
2. [`com.avira.couchdoop.demo.ExportDriver`](/src/main/java/com/avira/couchdoop/demo/ExportDriver.java)
  - Exports the recommendations computed by the recommender to Couchbase. It is
	assumed that the recommender outputs delimited text files where on each
	line there is the user ID followed by tab and a tab separated list of
	recommendations. Each recommendation is defined by a semicolon separated
	pair composed from article identifier and a numeric score value. The Hadoop
	job triggered by this driver is responsible to reformat the delimited text
	recommendations to the JSON format expected by the website and to publish
	them to Couchbase. Check ["data/recommender.txt"](/data/recommender.txt) to
	see how the input of this Hadoop job looks like and
	["data/recommendations.json"](/data/recommendations.json) the see how the
	output from Couchbase is formatted.

Check the source code to see how Couchdoop library works.

This projects only shows how to import and export data from Couchbase and how
to reformat it. The recommendation algorithm is not implemented since is it out
of the scope of this example.

Prerequisites
-------------

Before running the two drivers make sure you did the following steps:

1. Create a Couchbase bucket named `users` with password `secret`.
2. Add the document from ["data/sessions.json"](/data/sessions.json) in the
   created bucket. You can copy-paste the key and the value in Couchbase's web
   interface.
3. Create view `"by_date"` in design document `"sessions"` by using Couchbase's
   web interface. Copy-paste the view JavaScript code from
   ["src/main/javascript/view.js"](/src/main/javascript/view.js). Publish the
   view in production.
4. Edit ["couchdoop-site.xml"](couchdoop-site.xml) and change `couchbase.urls`
   property to match your Couchbase cluster connection node. Note all the other
   properties.
5. Put file ["data/recommender.txt"](/data/recommender.txt) in HDFS which is
   going to be used to test the export driver.
6. Clone the project with git and package it with Maven by running `mvn
   package` in its root directory.

Importing
---------

If the project packaging succeded you should have file
*"target/couchdoop-demo-1.0.0-SNAPSHOT-job.jar"*. If so you can run the import
driver with the following command:

```
hadoop jar target/couchdoop-demo-1.0.0-SNAPSHOT-job.jar \
    com.avira.couchdoop.demo.ImportDriver \
	-conf couchdoop-site.xml \
	imported_sessions/
```

The output will be written in HDFS in *"imported\_sessions"* directory and
should match ["data/imported_sessions.txt"](/data/imported_sessions.txt).

Note Hadoop properties `couchbase.designdoc.name`, `couchbase.view.name` and
`couchbase.view.keys` from ["couchdoop-site.xml"](/couchdoop-site.xml) which
are used by
[`CouchbaseViewInputFormat`](https://github.com/Avira/couchdoop/blob/master/src/main/java/com/avira/couchdoop/imp/CouchbaseViewInputFormat.java)
to query the view.

Exporting
---------

Run the export driver with the following command:

```
hadoop jar target/couchdoop-demo-1.0.0-SNAPSHOT-job.jar \
    com.avira.couchdoop.demo.ExportDriver \
	-conf couchdoop-site.xml \
	recommender.txt
```

After running the job you should see two Couchbase documents, one with key
*johnny* and another with key *tom* as sampled in
["data/recommendations.json"](/data/recommendations.json).
