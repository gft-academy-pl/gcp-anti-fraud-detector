![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/notifications-highlight.png?raw=true)

### Trigger sending email notifications with Cloud Function

**Create SendGrid Account**
- Use the Google Cloud Launcher to sign up for the SendGrid Email service.
- Create an API key:
-- Sign in to Sendgrid and go to Settings > API Keys.
-- Create an API key.
-- Select the permissions for the key. At a minimum, the key will need Mail send permissions to send email.
-- Click Save to create the key.
-- SendGrid generates a new key. This is the only copy of the key so make sure to copy the key and save it for later.

**Global variables**

```
# GOOGLE_CLOUD_PROJECT
export GCP_SENDGRID_API_KEY=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
export GCP_FROM_EMAIL=przemyslaw.juszkiewicz@gmail.com
export GCP_TO_EMAIL=przemyslaw.juszkiewicz@gmail.com
```

**Generate config.json**

```
sed -i 's/__GCP_SENDGRID_API_KEY__/'"$GCP_SENDGRID_API_KEY"'/g' config.json
sed -i 's/__GCP_FROM_EMAIL__/'"$GCP_FROM_EMAIL"'/g' config.json
sed -i 's/__GCP_TO_EMAIL__/'"$GCP_TO_EMAIL"'/g' config.json
```

**Deploy function**

```
cd ~/gcp-anti-fraud-detector/cloud-functions/email-notifications
npm install
gcloud beta functions deploy sendEmail --trigger-bucket=gs://${GCP_OUTPUT_BUCKET}
```

**Test function**

```
cd ~
touch test-file.txt
gsutil cp ~/test-file.txt gs://${GCP_OUTPUT_BUCKET}
```
