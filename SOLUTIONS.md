## Storage 
 - for fraud detector data input
 - for fraud detector data output

### Create 3 buckets

**Docs**
- gsutils mb: https://cloud.google.com/storage/docs/gsutil/commands/mb 
- bucket locations: https://cloud.google.com/storage/docs/bucket-locations

<details><summary><b>Answer</b></summary>
<pre><code>gsutil mb -c regional -l europe-west3 gs://gft-academy-fraud-detector-input/
gsutil mb -c regional -l europe-west3 gs://gft-academy-fraud-detector-output/
</code></pre>
</details>


### Upload sample data

**Docs**
- gsutils cp: https://cloud.google.com/storage/docs/gsutil/commands/cp

<details><summary><b>Answer</b></summary>
<pre><code>gsutil cp gs://gft-academy-fraud-detector-public-data/trades-small.csv gs://gft-academy-fraud-detector-input/
</code></pre>
</details>

## Anti Fraud ETL

Source repository: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-data-dataflow

### Create DataFlow bootstrap project

**Docs**
- https://cloud.google.com/dataflow/docs
- https://cloud.google.com/dataflow/docs/quickstarts/quickstart-java-maven

If you want to execute pipeline on the DataFlow you need to:
 1. install gsutils locally
 2. authenticate with command: ```gcloud auth login```

<details><summary><b>Answer</b></summary>
<pre><code>mvn archetype:generate \
      -DarchetypeArtifactId=google-cloud-dataflow-java-archetypes-examples \
      -DarchetypeGroupId=com.google.cloud.dataflow \
      -DarchetypeVersion=2.4.0 \
      -DgroupId=com.gft.academy \
      -DartifactId=gcp-anti-fraud-detector \
      -Dversion="0.1" \
      -DinteractiveMode=false \
      -Dpackage=com.gft.academy</code></pre>

<h4>Run locally</h4>
<pre><code>mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.WordCount \
      -Dexec.args="--output=./target/wordcount/ \
      --inputFile=gs://gft-academy-fraud-detector-public-data/kinglear.txt"
</code></pre>

<h4>Run on the DataFlow</h4>
<pre><code>mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.WordCount \
      -Dexec.args="--project=gft-swat-team \
      --inputFile=gs://gft-academy-fraud-detector-public-data/trades-small.csv \
      --output=gs://gft-academy-fraud-detector-output/wordcount \
      --stagingLocation=gs://gft-academy-fraud-detector-output/wordcount-staging \
      --runner=DataflowRunner"
</code></pre>
</details>

### Create own Pipeline to find the frauds ###

<details><summary><b>Answer</b></summary>
<ul>
 <li>Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-data-dataflow/blob/master/src/main/java/com/gft/academy/FraudDetector.java</li>
 <li>Test: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-data-dataflow/blob/master/src/test/java/com/gft/academy/FraudDetectorTest.java</li>
</ul>
 
<h4>Run locally</h4>
<pre><code>mvn clean compile exec:java \
       -Dexec.mainClass=com.gft.academy.FraudDetector \
       -Dexec.args="--output=./target/frauds/ \
       --inputFile=gs://gft-academy-fraud-detector-public-data/trades-small.csv"
</code></pre>
 
<h4>Run on the DataFlow</h4>
<pre><code>mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.FraudDetector \
      -Dexec.args="--project=gft-swat-team \
      --inputFile=gs://gft-academy-fraud-detector-public-data/trades-small.csv \
      --output=gs://gft-academy-fraud-detector-output/frauds \
      --stagingLocation=gs://gft-academy-fraud-detector-output/frauds-staging --runner=DataflowRunner"
</code></pre>
</details>

### Create Job Template ###

**Docs**
- https://cloud.google.com/dataflow/docs/templates/overview
- https://cloud.google.com/dataflow/docs/templates/creating-templates
- https://cloud.google.com/dataflow/docs/templates/executing-templates

<details><summary><b>Answer</b></summary>
<pre><code>mvn clean compile exec:java \
       -Dexec.mainClass=com.gft.academy.FraudDetector \
       -Dexec.args="--project=gft-swat-team \
       --templateLocation=gs://gft-academy-fraud-detector-output/templates/fraud-detector \
       --runner=DataflowRunner"
</code></pre>
</details>
