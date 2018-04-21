![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/notifications-highlight.png?raw=true)

### Trigger sending email notifications with Cloud Function

**Create Nexmo Account**
- Go to https://www.nexmo.com and sign up for the free account.
- API key and secret will be displayed on the main screen with €2 free credit.

**Code**

- Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/cloud-functions/sms-notifications/index.js

```
cd ~/gcp-anti-fraud-detector/cloud-functions/sms-notifications
npm install
```

**Global variables**

```
# GOOGLE_CLOUD_PROJECT
export GCP_NEXMO_KEY=acea9a02
export GCP_NEXMO_SECRET=jRntAbJ4xA7aNqq9
export GCP_NEXMO_TO=+48509644234
```

**Generate config.json**

```
sed -i 's/__GCP_NEXMO_KEY__/'"$GCP_NEXMO_KEY"'/g' config.json
sed -i 's/__GCP_NEXMO_SECRET__/'"$GCP_NEXMO_SECRET"'/g' config.json
sed -i 's/__GCP_NEXMO_TO__/'"$GCP_NEXMO_TO"'/g' config.json
```

**Deploy function**

```
gcloud beta functions deploy sendSms --trigger-bucket=gs://${GCP_OUTPUT_BUCKET}
```

**Test function**

```
cd ~
touch test-file.txt
gsutil cp ~/test-file.txt gs://${GCP_OUTPUT_BUCKET}
```
