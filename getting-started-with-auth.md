**Init project**

```
cd ~/gcp-anti-fraud-detector/cloud-functions/dataflow-notifications
npm install
```

**Configure project**

```
sed -i 's/__INPUT_BUCKET__/'"$GCP_INPUT_BUCKET"'/' ./config.json
sed -i 's/__OUTPUT_BUCKET__/'"$GCP_OUTPUT_BUCKET"'/' ./config.json
sed -i 's/__WORKSPACE_BUCKET__/'"$GCP_WORKSPACE_BUCKET"'/' ./config.json
cat config.json
```

**Test API call with Application Default Credentials**

```
npm run test-auth-default
```

**Test API call with System Account via JWT**

* Navigate to: https://console.cloud.google.com/iam-admin/serviceaccounts, select Project and create Service Account with "Dataflow Admin, Dataflow Developer, Dataflow Viewer, Dataflow Worker, Storage Object Viewer" permissions and generate private key

![System Account](https://raw.githubusercontent.com/gft-academy-pl/gcp-anti-fraud-detector/master/assets/system-account.png)

* Upload key (drag and drop via CloudShell) to `~/gcp-anti-fraud-detector/cloud-functions/dataflow-notifications` and rename it to `jwt.keys.json`

```
npm run test-auth-jwt
```
