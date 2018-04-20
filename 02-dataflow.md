![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/dataflow-highlight.png?raw=true)

## Agenda
- global concept of our fround detection
- what is dataflow
- what is apache beam
- quickly describe project structure
- descibe the project code 

## Anti Fraud ETL


### Create own Pipeline to find the frauds ###


<ul>
 <li>Source repository: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-data-dataflow</li>
 <li>Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-dataflow/blob/master/src/main/java/com/gft/academy/FraudDetector.java</li>
 <li>Test: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-dataflow/blob/master/src/test/java/com/gft/academy/FraudDetectorTest.java</li>
</ul>

**Prepare**

```
cd ~ && \
rm -rf gcp-anti-fraud-detector-dataflow && \
git clone git@github.com:gft-academy-pl/gcp-anti-fraud-detector-dataflow.git && \
cd gcp-anti-fraud-detector-dataflow
```
  
**Run locally**

```
mvn clean compile exec:java \
       -Dexec.mainClass=com.gft.academy.FraudDetector \
       -Dexec.args="--output=./target/frauds/ \
       --inputFile=gs://gft-academy-fraud-detector-public-data/trades-small.csv"
```
 
**Run on the DataFlow**

Enable API first: https://console.cloud.google.com/apis/library/dataflow.googleapis.com

```
mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.FraudDetector \
      -Dexec.args="--project=${GOOGLE_CLOUD_PROJECT} \
      --inputFile=gs://gft-academy-fraud-detector-public-data/trades-small.csv \
      --output=${GCP_OUTPUT_BUCKET}frauds \
      --stagingLocation=${GCP_OUTPUT_BUCKET}frauds-staging --runner=DataflowRunner"
```

### Create job template ###

**Docs**
- https://cloud.google.com/dataflow/docs/templates/overview
- https://cloud.google.com/dataflow/docs/templates/creating-templates
- https://cloud.google.com/dataflow/docs/templates/executing-templates

<details><summary><b>Answer</b></summary>

```
mvn clean compile exec:java \
       -Dexec.mainClass=com.gft.academy.FraudDetector \
       -Dexec.args="--project=${GOOGLE_CLOUD_PROJECT} \
       --templateLocation=${GCP_OUTPUT_BUCKET}templates/fraud-detector \
       --runner=DataflowRunner"
```
