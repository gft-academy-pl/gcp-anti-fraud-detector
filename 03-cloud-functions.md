## Cloud Functions

### Getting started with Cloud Functions

**Docs**
- https://cloud.google.com/functions/docs/
- https://github.com/GoogleCloudPlatform/cloud-functions-emulator

<details><summary><b>Answer</b></summary>

**Create sample function**

```
cd ~ && \
rm -rf gcp-anti-fraud-detector-cloud-functions && \
mkdir -p gcp-anti-fraud-detector-cloud-functions && \
cd gcp-anti-fraud-detector-cloud-functions && \
touch index.js && \
echo 'exports.helloworldFn = (req, res) => res.send("Hello, World!");' > index.js
```
 
**Install emulator globally**

```
sudo npm install -g @google-cloud/functions-emulator
```

**Run emulator and specify working projectId**

```
sudo functions start
```

**Deploy sample function to emulator**

```
functions deploy helloworldFn --trigger-http
```

**Call sample function**

```
functions call helloworldFn
```

**Observe logs**

```
functions logs read
```

**Deploy function to cloud**

- Enable API first: https://console.cloud.google.com/apis/library/cloudfunctions.googleapis.com

```
gcloud beta functions deploy helloworldFn --trigger-http
```

</details>

### Authorized calls to DataFlow API

**Docs**
- https://developers.google.com/api-client-library/
- https://github.com/google/google-api-nodejs-client/#google-apis-nodejs-client
- https://github.com/google/google-api-nodejs-client/#authentication-and-authorization
- https://github.com/google/google-auth-library-nodejs#choosing-the-correct-credential-type-automatically
- http://google.github.io/google-api-nodejs-client/
- http://google.github.io/google-api-nodejs-client/classes/_apis_dataflow_v1b3_.dataflow.html

<details><summary><b>Answer</b></summary>

**Prepare**

```
cd ~ && \
rm -rf gcp-anti-fraud-detector-cloud-functions && \
git clone git@github.com:gft-academy-pl/gcp-anti-fraud-detector-cloud-functions.git && \
cd gcp-anti-fraud-detector-cloud-functions && \
npm install
```

**Test API call with Application Default Credentials**

```
npm run test-auth-default
```

**Test API call with JWT**

* Create Service Account https://console.cloud.google.com/iam-admin/serviceaccounts with Cloud Functions Admin, DataFlow Admin, Storage Viewer permissions and generate private key
* Upload key to the gcp-anti-fraud-detector-cloud-functions project

```
npm run test-auth-jwt
```

</details>

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
