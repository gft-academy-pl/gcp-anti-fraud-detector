![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/notifications-highlight.png?raw=true)

### Trigger sending email notifications with Cloud Function

**Create SendGrid Account**
- Navigate to: Launcher -> SendGrid Email API and signup for free account.
- Create an API key:
  - Sign in to Sendgrid and go to Settings > API Keys.
  - Create an API key.
  - Select the permissions for the key. At a minimum, the key will need Mail send permissions to send email.
  - Click Save to create the key.
  - SendGrid generates a new key. This is the only copy of the key so make sure to copy the key and save it for later.

**Code**

- Implementation: https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/cloud-functions/email-notifications/index.js

```
cd ~/gcp-anti-fraud-detector/cloud-functions/email-notifications
npm install
```

**Global variables**

```
# GOOGLE_CLOUD_PROJECT
export GCP_SENDGRID_API_KEY=[sendgrid API Key]
export GCP_FROM_EMAIL=[from email address]
export GCP_TO_EMAIL=[to email address]
```

**Generate config.json**

```
sed -i 's/__GCP_SENDGRID_API_KEY__/'"$GCP_SENDGRID_API_KEY"'/g' config.json
sed -i 's/__GCP_FROM_EMAIL__/'"$GCP_FROM_EMAIL"'/g' config.json
sed -i 's/__GCP_TO_EMAIL__/'"$GCP_TO_EMAIL"'/g' config.json
```

**Deploy function**

```
gcloud beta functions deploy sendEmail --trigger-bucket=gs://${GCP_OUTPUT_BUCKET}
```

## Navigation

- [Previous Step](./03-cloud-functions.md)
- [Next Step](./05-sms-notifications.md)
