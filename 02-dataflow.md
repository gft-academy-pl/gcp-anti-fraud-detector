![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/dataflow-highlight.png?raw=true)

## Agenda
- global concept of our fround detection
- what is dataflow
- what is apache beam
- quickly describe project structure
- descibe the project code 

## DataFlow - pipeline to find the frauds

**Code**
- Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/dataflow/detecting-frauds/src/main/java/com/gft/academy/FraudDetector.java
- Test: https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/dataflow/detecting-frauds/src/test/java/com/gft/academy/FraudDetectorTest.java

```
cd ~/gcp-anti-fraud-detector/dataflow/detecting-frauds
```
 
**Run locally**

- Input: public dataset
- Output: local directory

```
mvn clean compile exec:java \
       -Dexec.mainClass=com.gft.academy.FraudDetector \
       -Dexec.args="--output=./target/frauds/ \
       --inputFile=gs://gft-academy-fraud-detector-public-data/trades-small.csv"
```
 
**Run on the DataFlow**

Enable API first: https://console.cloud.google.com/apis/library/dataflow.googleapis.com

- Input: public dataset
- Output: output bucket

```
mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.FraudDetector \
      -Dexec.args="--project=${GOOGLE_CLOUD_PROJECT} \
      --inputFile=gs://${GCP_INPUT_BUCKET}/trades-small.csv \
      --output=gs://${GCP_OUTPUT_BUCKET}/frauds \
      --tempLocation=gs://${GCP_WORKSPACE_BUCKET}/frauds-tmp \
      --runner=DataflowRunner"
```

**Create job template**

```
mvn clean compile exec:java \
       -Dexec.mainClass=com.gft.academy.FraudDetector \
       -Dexec.args="--project=${GOOGLE_CLOUD_PROJECT} \
       --templateLocation=gs://${GCP_OUTPUT_BUCKET}/templates/fraud-detector \
       --tempLocation=gs://${GCP_WORKSPACE_BUCKET}/frauds-tmp \
       --runner=DataflowRunner"
```

**Docs**
- https://cloud.google.com/dataflow/docs
- https://cloud.google.com/dataflow/docs/quickstarts/quickstart-java-maven
- https://cloud.google.com/dataflow/docs/templates/overview
- https://cloud.google.com/dataflow/docs/templates/creating-templates
- https://cloud.google.com/dataflow/docs/templates/executing-templates

## Navigation

- [Previous Step](./01-storage.md)
- [Next Step](./03-cloud-functions.md)
