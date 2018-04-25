const fs = require("fs");
const path = require('path');
const CONFIG = JSON.parse(fs.readFileSync(path.join(__dirname, 'config.json')));
const Nexmo = require('nexmo');
const nexmo = new Nexmo({
  apiKey: CONFIG.GCP_NEXMO_KEY,
  apiSecret: CONFIG.GCP_NEXMO_SECRET
});

exports.sendSms = (event) => {
  const file = event.data;
  if(file.name.startsWith('.')){
    return Promise.resolve();
  }
  const from = 'GFT-ACADEMY';
  const to = CONFIG.GCP_NEXMO_TO;
  const text = `All details can be found in gs://${file.bucket}/${file.name}`;
  return nexmo.message.sendSms(from, to, text)	
};
