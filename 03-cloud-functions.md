![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/cloud-functions-highlight.png?raw=true)

## Agenda
- cloud functions overview
- enable api for dataflow
- service account setup
- function deployment (do not describe the js code) 

## Cloud Functions - Dataflow Notification

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

**Code**
- Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/cloud-functions/dataflow-notifications/index.js

```
cd ~/gcp-anti-fraud-detector/cloud-functions/dataflow-notifications
npm install
```

**Test API call with Application Default Credentials**

```
npm run test-auth-default
```

**Test API call with JWT**

* Navigate to: https://console.cloud.google.com/iam-admin/serviceaccounts, select Project and create Service Account  with Cloud Functions Admin, DataFlow Admin, Storage Viewer permissions and generate private key
![System Account](https://raw.githubusercontent.com/gft-academy-pl/gcp-anti-fraud-detector/master/assets/system-account.png)
![System Account](https://raw.githubusercontent.com/gft-academy-pl/gcp-anti-fraud-detector/master/assets/roles.png)
* Upload key (drag and drop via CloudShell) to `~/gcp-anti-fraud-detector/cloud-functions/dataflow-notifications` and rename it to `jwt.keys.json`

```
npm run test-auth-jwt
```

### Trigger DataFlow job template with Cloud Function

**Docs**
- https://shinesolutions.com/2017/03/23/triggering-dataflow-pipelines-with-cloud-functions/ - with templates
- https://cloud.google.com/blog/big-data/2016/04/scheduling-dataflow-pipelines-using-app-engine-cron-service-or-cloud-functions - with spawning processess
- https://cloud.google.com/dataflow/docs/templates/executing-templates#using-the-google-api-client-libraries

<details><summary><b>Answer</b></summary>
<ul>
 <li>Source repository: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-cloud-functions</li>
 <li>Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-cloud-functions/blob/master/inputDataTrigger/index.js</li>
 <li>Test: https://github.com/gft-academy-pl/gcp-anti-fraud-detector-cloud-functions/blob/master/inputDataTrigger/index.test.js</li>
</ul>
 
 **Simulate triggering DataFlow job**
 
 ```
sudo functions logs clear && \
functions deploy triggerDataflowFn --trigger-bucket=${GCP_INPUT_BUCKET} && \
functions call triggerDataflowFn && \
functions logs read
```

**Deploy function to cloud**

- Enable API first: https://console.cloud.google.com/apis/library/cloudfunctions.googleapis.com

```
gcloud beta functions deploy triggerDataflowFn --trigger-bucket=${GCP_INPUT_BUCKET}
```
</details>
