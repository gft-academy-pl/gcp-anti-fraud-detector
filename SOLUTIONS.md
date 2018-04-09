## Storage 
 - for dataflow project files
 - for fraud detector data input
 - for fraud detector data output

### Create 3 buckets

**Docs**
- gsutils mb: https://cloud.google.com/storage/docs/gsutil/commands/mb 
- bucket locations: https://cloud.google.com/storage/docs/bucket-locations

**Answer**

```
gsutil mb -c regional -l europe-west3 gs://gft-academy-fraud-detector-staging/
gsutil mb -c regional -l europe-west3 gs://gft-academy-fraud-detector-input/
gsutil mb -c regional -l europe-west3 gs://gft-academy-fraud-detector-output/
```

### Upload sample data

**Docs**
- gsutils cp: https://cloud.google.com/storage/docs/gsutil/commands/cp

**Answer**

```
gsutil cp trades.csv gs://gft-academy-fraud-detector-input
```

## Antifraud ETL

### Create bootstrap project

**Docs**
- https://cloud.google.com/dataflow/docs
- https://cloud.google.com/dataflow/docs/quickstarts/quickstart-java-maven


**Answer**

```
mvn archetype:generate \
      -DarchetypeArtifactId=google-cloud-dataflow-java-archetypes-examples \
      -DarchetypeGroupId=com.google.cloud.dataflow \
      -DarchetypeVersion=1.9.1 \
      -DgroupId=com.gft.academy \
      -DartifactId=gcp-anti-fraud-detector \
      -Dversion="0.1" \
      -DinteractiveMode=false \
      -Dpackage=com.gft.academy
```
