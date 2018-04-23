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
export GCP_NEXMO_KEY=[secret from sendgrid]
export GCP_NEXMO_SECRET=[secret from sendgrid]
export GCP_NEXMO_TO=[your phone number like: +48509123456]
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
gsutil cp gs://gft-academy-fraud-detector-public-data/frauds-trades-small.txt gs://${GCP_OUTPUT_BUCKET}
```

## Navigation

- [Previous Step](./04-email-notifications.md)
- [Next Step](./06-executing-pipeline.md)
