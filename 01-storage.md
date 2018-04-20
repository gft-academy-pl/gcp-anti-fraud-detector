![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/storage-highlight.png?raw=true)

## AGENDA
- describe storage theory
- gsutil 
- create buckets 
- upload file 

## Storage 
 - for fraud detector data input
 - for fraud detector data output

### Create 3 buckets

**Docs**

- gsutils mb: https://cloud.google.com/storage/docs/gsutil/commands/mb 
- bucket locations: https://cloud.google.com/storage/docs/bucket-locations

<details><summary><b>Answer</b></summary>
 
```
gsutil mb -c regional -l europe-west3 ${GCP_INPUT_BUCKET}
gsutil mb -c regional -l europe-west3 ${GCP_OUTPUT_BUCKET}
```
</details>


### Upload sample data

**Docs**
- gsutils cp: https://cloud.google.com/storage/docs/gsutil/commands/cp

<details><summary><b>Answer</b></summary>

```
gsutil cp gs://gft-academy-fraud-detector-public-data/trades-small.csv ${GCP_INPUT_BUCKET}
```

</details>
