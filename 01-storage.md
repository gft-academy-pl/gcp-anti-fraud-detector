![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/storage-highlight.png?raw=true)

## Agenda
- describe storage theory
- gsutil 
- create buckets 
- upload file 

## Storage 
 - for fraud detector data input
 - for fraud detector data output

### Create 2 buckets

**Docs**

- gsutils mb: https://cloud.google.com/storage/docs/gsutil/commands/mb 
- bucket locations: https://cloud.google.com/storage/docs/bucket-locations
 
```
gsutil mb -c regional -l europe-west3 ${GCP_INPUT_BUCKET}
gsutil mb -c regional -l europe-west3 ${GCP_OUTPUT_BUCKET}
```

### Upload sample data

**Docs**
- gsutils cp: https://cloud.google.com/storage/docs/gsutil/commands/cp

```
gsutil cp gs://gft-academy-fraud-detector-public-data/trades-small.csv ${GCP_INPUT_BUCKET}
```
