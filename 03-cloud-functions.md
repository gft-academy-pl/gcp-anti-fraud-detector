![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/cloud-functions-highlight.png?raw=true)

## Agenda
- Cloud functions
- Enable api for dataflow
- Service account setup
- Function deployment (do not describe the js code) 

## Cloud Functions 

Google Cloud Functions is a serverless execution environment for building and connecting cloud services. With Cloud Functions you write simple, single-purpose functions that are attached to events emitted from your cloud infrastructure and services. Your Cloud Function is triggered when an event being watched is fired. Your code executes in a fully managed environment. There is no need to provision any infrastructure or worry about managing any servers.

Cloud Functions are written in Javascript and execute in a Node.js v6.11.5 environment on Google Cloud Platform. You can take your Cloud Function and run it in any standard Node.js runtime which makes both portability and local testing a breeze.

**Serverless**

Cloud Functions removes the work of managing servers, configuring software, updating frameworks, and patching operating systems. The software and infrastructure are fully managed by Google so that you just add code. Furthermore, provisioning of resources happens automatically in response to events. This means that a function can scale from a few invocations a day to many millions of invocations without any work from you.

**Events**
- HTTP—invoke functions directly via HTTP requests
- Cloud Storage
- Cloud Pub/Sub
- Firebase (DB, Storage, Analytics, Auth)

**Trigger**
- HTTP:	--trigger-http
- Google Cloud Storage:	--trigger-bucket [BUCKET NAME]
- Google Cloud Pub/Sub:	--trigger-topic [TOPIC NAME]

https://cloud.google.com/functions/docs/concepts/overview 

### Dataflow notification / trigger

**Code**
- Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/cloud-functions/dataflow-notifications/index.js

```
cd ~/gcp-anti-fraud-detector/cloud-functions/dataflow-notifications
npm install
```

**Generate config.json**

```
 sed -i 's/__INPUT_BUCKET__/'"$GCP_INPUT_BUCKET"'/' ./config.json
 sed -i 's/__OUTPUT_BUCKET__/'"$GCP_OUTPUT_BUCKET"'/' ./config.json
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

### Deploy function to the cloud

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
