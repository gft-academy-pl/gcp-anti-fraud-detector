![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/cloud-functions-highlight.png?raw=true)

## Agenda
- cloud functions overview
- enable api for dataflow
- service account setup
- function deployment (do not describe the js code) 

## Cloud Functions - Dataflow Notification

**Code**
- Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/cloud-functions/dataflow-notifications/index.js

```
cd ~/gcp-anti-fraud-detector/cloud-functions/dataflow-notifications
npm install
```

**Generate config.json**

```
 sed -i -- 's/__INPUT_BUCKET__/'"$GCP_INPUT_BUCKET"'/' ./config.json
 sed -i -- 's/__OUTPUT_BUCKET__/'"$GCP_OUTPUT_BUCKET"'/' ./config.json
 ```
 
**Test API call with Application Default Credentials**

```
npm run test-auth-default
```

**Test API call with System Account via JWT**

* Navigate to: https://console.cloud.google.com/iam-admin/serviceaccounts, select Project and create Service Account with "Dataflow Developer, Dataflow Viewer, Dataflow Worker, Storage Object Viewer" permissions and generate private key

![System Account](https://raw.githubusercontent.com/gft-academy-pl/gcp-anti-fraud-detector/master/assets/system-account.png)

* Upload key (drag and drop via CloudShell) to `~/gcp-anti-fraud-detector/cloud-functions/dataflow-notifications` and rename it to `jwt.keys.json`

```
npm run test-auth-jwt
```

**Deploy function to cloud**

- Enable API first: https://console.cloud.google.com/apis/library/cloudfunctions.googleapis.com

```
gcloud beta functions deploy triggerDataflowFn --trigger-bucket=gs://${GCP_INPUT_BUCKET}
```

**Test**

```
gsutil cp gs://gft-academy-fraud-detector-public-data/trades-small.csv gs://${GCP_INPUT_BUCKET}
```

**Docs**
- https://cloud.google.com/functions/docs/
- https://shinesolutions.com/2017/03/23/triggering-dataflow-pipelines-with-cloud-functions/ - with templates
- https://cloud.google.com/blog/big-data/2016/04/scheduling-dataflow-pipelines-using-app-engine-cron-service-or-cloud-functions - with spawning processess
- https://cloud.google.com/dataflow/docs/templates/executing-templates#using-the-google-api-client-libraries
- https://developers.google.com/api-client-library/
- https://github.com/google/google-api-nodejs-client/#google-apis-nodejs-client
- https://github.com/google/google-api-nodejs-client/#authentication-and-authorization
- https://github.com/google/google-auth-library-nodejs#choosing-the-correct-credential-type-automatically
- http://google.github.io/google-api-nodejs-client/
- http://google.github.io/google-api-nodejs-client/classes/_apis_dataflow_v1b3_.dataflow.html
