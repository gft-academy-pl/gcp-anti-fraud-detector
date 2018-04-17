## Init

```
# GOOGLE_CLOUD_PROJECT
export GCP_INPUT_BUCKET=gs://gft-academy-fraud-detector-input/
export GCP_OUTPUT_BUCKET=gs://gft-academy-fraud-detector-output/
```

## Storage 
 - for fraud detector data input
 - for fraud detector data output

### Create 3 buckets

**Docs**

- gsutils mb: https://cloud.google.com/storage/docs/gsutil/commands/mb 
- bucket locations: https://cloud.google.com/storage/docs/bucket-locations

<details><summary><b>Answer</b></summary>
<pre><code>gsutil mb -c regional -l europe-west3 ${GCP_INPUT_BUCKET}
gsutil mb -c regional -l europe-west3 ${GCP_OUTPUT_BUCKET}
</code></pre>
</details>


### Upload sample data

**Docs**
- gsutils cp: https://cloud.google.com/storage/docs/gsutil/commands/cp

<details><summary><b>Answer</b></summary>
<pre><code>gsutil cp gs://gft-academy-fraud-detector-public-data/trades-small.csv ${GCP_INPUT_BUCKET}
</code></pre>
</details>

## Anti Fraud ETL

### Getting started with DataFlow - bootstrap initial project

**Docs**
- https://cloud.google.com/dataflow/docs
- https://cloud.google.com/dataflow/docs/quickstarts/quickstart-java-maven

<details><summary><b>Answer</b></summary>
<pre><code> cd ~
mvn archetype:generate \
      -DarchetypeArtifactId=google-cloud-dataflow-java-archetypes-examples \
      -DarchetypeGroupId=com.google.cloud.dataflow \
      -DarchetypeVersion=2.4.0 \
      -DgroupId=com.gft.academy \
      -DartifactId=gcp-anti-fraud-detector \
      -Dversion="0.1" \
      -DinteractiveMode=false \
      -Dpackage=com.gft.academy
cd gcp-anti-fraud-detector</code></pre>

**Run locally**
<pre><code>mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.WordCount \
      -Dexec.args="--output=./target/wordcount/ \
      --inputFile=gs://gft-academy-fraud-detector-public-data/kinglear.txt"
</code></pre>

**Run on the DataFlow**

- Enable API first: https://console.cloud.google.com/apis/library/dataflow.googleapis.com

<pre><code>mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.WordCount \
      -Dexec.args="--project=${GOOGLE_CLOUD_PROJECT} \
      --inputFile=gs://gft-academy-fraud-detector-public-data/trades-small.csv \
      --output=${GCP_OUTPUT_BUCKET}wordcount \
      --stagingLocation=${GCP_OUTPUT_BUCKET}wordcount-staging \
      --runner=DataflowRunner"
</code></pre>
</details>

### Create own Pipeline to find the frauds ###

<details><summary><b>Answer</b></summary>
<ul>
 <li>Source repository: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-data-dataflow</li>
 <li>Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-dataflow/blob/master/src/main/java/com/gft/academy/FraudDetector.java</li>
 <li>Test: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-dataflow/blob/master/src/test/java/com/gft/academy/FraudDetectorTest.java</li>
</ul>

**Prepare**

```
cd ~ 
rm -rf gcp-anti-fraud-detector-data-dataflow
git clone git@github.com:gft-academy-pl/gcp-anti-fraud-detector-dataflow.git
cd gcp-anti-fraud-detector-data-dataflow
```
  
**Run locally**

```
mvn clean compile exec:java \
       -Dexec.mainClass=com.gft.academy.FraudDetector \
       -Dexec.args="--output=./target/frauds/ \
       --inputFile=gs://gft-academy-fraud-detector-public-data/trades-small.csv"
```
 
**Run on the DataFlow**
```
mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.FraudDetector \
      -Dexec.args="--project=${GOOGLE_CLOUD_PROJECT} \
      --inputFile=gs://gft-academy-fraud-detector-public-data/trades-small.csv \
      --output=${GCP_OUTPUT_BUCKET}frauds \
      --stagingLocation=${GCP_OUTPUT_BUCKET}frauds-staging --runner=DataflowRunner"
```
</details>

### Create job template ###

**Docs**
- https://cloud.google.com/dataflow/docs/templates/overview
- https://cloud.google.com/dataflow/docs/templates/creating-templates
- https://cloud.google.com/dataflow/docs/templates/executing-templates

<details><summary><b>Answer</b></summary>
<pre><code>mvn clean compile exec:java \
       -Dexec.mainClass=com.gft.academy.FraudDetector \
       -Dexec.args="--project=${GOOGLE_CLOUD_PROJECT} \
       --templateLocation=${GCP_OUTPUT_BUCKET}templates/fraud-detector \
       --runner=DataflowRunner"
</code></pre>
</details>

### Execute job from custom template via web interface

**Docs**
- https://cloud.google.com/dataflow/docs/templates/executing-templates

### Getting started with Cloud Functions

**Docs**
- https://cloud.google.com/functions/docs/
- https://github.com/GoogleCloudPlatform/cloud-functions-emulator

<details><summary><b>Answer</b></summary>

**Create sample function**
<pre><code>
cd ~
mkdir -p gcp-anti-fraud-detector-cloud-functions/sampleFn
cd gcp-anti-fraud-detector-cloud-functions/sampleFn
touch index.js
echo 'exports.helloWorld = (req, res) => res.send("Hello, World!");' > index.js
</code></pre>
 
**Install emulator globally**
<pre><code>sudo npm install -g @google-cloud/functions-emulator</code></pre>

**Run emulator and specify working projectId**
<pre><code>sudo functions start</code></pre>

**Deploy sample function**
<pre><code>functions deploy sampleFn --trigger-http</code></pre>

**Call sample function**
<pre><code>functions call sampleFn</code></pre>

**Observe logs**
<pre><code>functions logs read</code></pre>
</details>

**Deploy function to cloud**

### Authorized calls to DataFlow API using Application Default Credentials

**Docs**
- https://developers.google.com/api-client-library/
- https://github.com/google/google-api-nodejs-client/#google-apis-nodejs-client
- https://github.com/google/google-api-nodejs-client/#authentication-and-authorization
- https://github.com/google/google-auth-library-nodejs#choosing-the-correct-credential-type-automatically
- http://google.github.io/google-api-nodejs-client/
- http://google.github.io/google-api-nodejs-client/classes/_apis_dataflow_v1b3_.dataflow.html


### Trigger DataFlow with Cloud Function

**Docs**
- https://shinesolutions.com/2017/03/23/triggering-dataflow-pipelines-with-cloud-functions/ - with templates
- https://cloud.google.com/blog/big-data/2016/04/scheduling-dataflow-pipelines-using-app-engine-cron-service-or-cloud-functions - with spawning processess
- https://cloud.google.com/dataflow/docs/templates/executing-templates#using-the-google-api-client-libraries

<details><summary><b>Answer</b></summary>
<ul>
 <li>Source repository: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-cloud-functions</li>
 <li>Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-cloud-functions/blob/master/inputDataTrigger/index.js</li>
 <li>Test: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-cloud-functions/blob/master/inputDataTrigger/index.test.js</li>
</ul>
 
 **Deploy, run, observe function logs via simulator**
<pre><code>sudo functions logs clear && functions deploy inputDataTriggerFn --trigger-bucket=gft-academy-fraud-dete
ctor-input && functions call inputDataTriggerFn && functions logs read</code></pre></code></pre>

 
**Deploy to GCP**
<pre><code>TBD</code></pre></details>

## TODO
- Use token based authentication inside cloud functions
