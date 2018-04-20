## Getting started with Cloud Functions

**Docs**
- https://cloud.google.com/functions/docs/
- https://github.com/GoogleCloudPlatform/cloud-functions-emulator

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
