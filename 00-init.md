![GCP Logo](https://raw.githubusercontent.com/gft-academy-pl/gcp-anti-fraud-detector/master/assets/google-platform.png)

## Agenda

- introduction to GCP
- introduction to GCP shell and editor

## Init

**Global variables**

```
# GOOGLE_CLOUD_PROJECT
export GCP_INPUT_BUCKET=gft-academy-fraud-detector-input-${LOGNAME}
export GCP_OUTPUT_BUCKET=gft-academy-fraud-detector-output-${LOGNAME}
```

**Fetch code**

```
git clone https://github.com/gft-academy-pl/gcp-anti-fraud-detector.git
```

**Configure**

```
sed -i -- 's/__INPUT_BUCKET__/'"$GCP_INPUT_BUCKET"'/' ./cloud-functions/dataflow-notifications/config.json
sed -i -- 's/__OUTPUT_BUCKET__/'"$GCP_OUTPUT_BUCKET"'/' ./cloud-functions/dataflow-notifications/config.json
```

