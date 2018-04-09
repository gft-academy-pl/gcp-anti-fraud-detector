## Storage 
 - for fraud detector data input
 - for fraud detector data output

### Create 3 buckets

**Docs**
- gsutils mb: https://cloud.google.com/storage/docs/gsutil/commands/mb 
- bucket locations: https://cloud.google.com/storage/docs/bucket-locations

**Answer**

```
gsutil mb -c regional -l europe-west3 gs://gft-academy-fraud-detector-input/
gsutil mb -c regional -l europe-west3 gs://gft-academy-fraud-detector-output/
```

### Upload sample data

**Docs**
- gsutils cp: https://cloud.google.com/storage/docs/gsutil/commands/cp

**Answer**

```
gsutil cp gs://gft-academy-fraud-detector-public-data/trades.csv gs://gft-academy-fraud-detector-input/
```

## Antifraud ETL

### Create DataFlow bootstrap project

**Docs**
- https://cloud.google.com/dataflow/docs
- https://cloud.google.com/dataflow/docs/quickstarts/quickstart-java-maven


**Answer**

Create project from MavenArchetype:

```
mvn archetype:generate \
      -DarchetypeArtifactId=google-cloud-dataflow-java-archetypes-examples \
      -DarchetypeGroupId=com.google.cloud.dataflow \
      -DarchetypeVersion=2.4.0 \
      -DgroupId=com.gft.academy \
      -DartifactId=gcp-anti-fraud-detector \
      -Dversion="0.1" \
      -DinteractiveMode=false \
      -Dpackage=com.gft.academy
```

Run sample flow on public dataset:

```
mvn compile exec:java \
      -Dexec.mainClass=com.gft.academy.WordCount \
      -Dexec.args="--output=./output/"
```

### Run an Example Pipeline on the Cloud Dataflow Service ###

**Answer**

```
 mvn compile exec:java \
      -Dexec.mainClass=com.gft.academy.WordCount \
      -Dexec.args="--project=<my-cloud-project> \
      --stagingLocation=gs://gft-academy-fraud-detector-output/staging/ \
      --inputFile=gs://gft-academy-fraud-detector-input/trades.csv \
      --output=gs://gft-academy-fraud-detector-output/output \
      --runner=DataflowRunner"
```

### Create own Pipeline to find the frauds ###

**Answer**

```

```
