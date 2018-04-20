![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/storage-highlight.png?raw=true)

## Agenda
- Cloud Storage (describe storage theory)
- gsutil 
- create buckets 
- upload file 

## Cloud Storage

### The idea

Google Cloud Storage allows world-wide storage and retrieval of any amount of data at any time. You can use Google Cloud Storage for a range of scenarios including serving website content, storing data for archival and disaster recovery, or distributing large data objects to users via direct download ([Google documentation]).




[Google documentation]: https://cloud.google.com/storage/docs/

## Storage 
 - for fraud detector data input
 - for fraud detector data output

### Create 2 buckets

**Docs**

- gsutils mb: https://cloud.google.com/storage/docs/gsutil/commands/mb 
- bucket locations: https://cloud.google.com/storage/docs/bucket-locations
 
```
gsutil mb -c regional -l europe-west3 gs://${GCP_INPUT_BUCKET}
gsutil mb -c regional -l europe-west3 gs://${GCP_OUTPUT_BUCKET}
```

### Upload sample data

**Docs**
- gsutils cp: https://cloud.google.com/storage/docs/gsutil/commands/cp

```
gsutil cp gs://gft-academy-fraud-detector-public-data/trades-small.csv gs://${GCP_INPUT_BUCKET}
```
