### Getting started with DataFlow - bootstrap initial project

**Docs**
- https://cloud.google.com/dataflow/docs
- https://cloud.google.com/dataflow/docs/quickstarts/quickstart-java-maven

```
cd ~
mvn archetype:generate \
      -DarchetypeArtifactId=google-cloud-dataflow-java-archetypes-examples \
      -DarchetypeGroupId=com.google.cloud.dataflow \
      -DarchetypeVersion=2.4.0 \
      -DgroupId=com.gft.academy \
      -DartifactId=gcp-anti-fraud-detector \
      -Dversion="0.1" \
      -DinteractiveMode=false \
      -Dpackage=com.gft.academy
cd gcp-anti-fraud-detector
```

**Run locally**

```
mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.WordCount \
      -Dexec.args="--output=./target/wordcount/ \
      --inputFile=gs://gft-academy-fraud-detector-public-data/kinglear.txt"
```

**Run on the DataFlow**

- Enable API first: https://console.cloud.google.com/apis/library/dataflow.googleapis.com

```
mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.WordCount \
      -Dexec.args="--project=${GOOGLE_CLOUD_PROJECT} \
      --inputFile=gs://gft-academy-fraud-detector-public-data/trades-small.csv \
      --output=${GCP_OUTPUT_BUCKET}wordcount \
      --stagingLocation=${GCP_OUTPUT_BUCKET}wordcount-staging \
      --runner=DataflowRunner"
```

### Execute job from custom template via web interface

**Docs**
- https://cloud.google.com/dataflow/docs/templates/executing-templates
