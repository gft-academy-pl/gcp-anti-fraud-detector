![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/storage-highlight.png?raw=true)

## Agenda
- Storage options
- Cloud Storage
- gsutil 
- Storage - exercises
  - Create bucket
  - Upload file 

## Storage options
[storage_options]: https://cloud.google.com/images/storage/storage-classes-desktop.svg "https://cloud.google.com/images/storage/storage-classes-desktop.svg"

## Cloud Storage

### Concept & Purpose

Google Cloud Storage allows world-wide storage and retrieval of any amount of data at any time. You can use Google Cloud Storage for a range of scenarios including serving website content, storing data for archival and disaster recovery, or distributing large data objects to users via direct download ([Google documentation]).

Widely accessible online disk space with pricing dependent on type, frequency of use and volume of the data, which is usually used to host static content and store cloud computation results.

### Storage types
![alt text][storage_types]

[Google documentation]: https://cloud.google.com/storage/docs/
[storage_types]: https://cloud.google.com/images/storage/storage-classes-desktop.svg "https://cloud.google.com/images/storage/storage-classes-desktop.svg"

## gsutil 

gsutil is a Python application that lets you access Cloud Storage from the command line (uses official [Google Cloud Storage REST API](https://cloud.google.com/storage/docs/apis) behind the scenes).

Example usages of gsutil:
* Creating and deleting buckets (no updates!).
* Uploading, downloading, deleting, moving, copying and renaming objects (files).
* Editing object metadata.
* Listing buckets and objects.
* Editing object and bucket ACLs.

More info can be found in [gsutil documentation](https://cloud.google.com/storage/docs/gsutil) and using `gsutil help`

## Storage - exercises
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
