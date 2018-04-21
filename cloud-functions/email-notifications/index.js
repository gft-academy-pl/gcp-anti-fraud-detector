const fs = require("fs");
const path = require('path');
const sgMail = require('@sendgrid/mail');

const CONFIG = JSON.parse(fs.readFileSync(path.join(__dirname, 'config.json')));

sgMail.setApiKey(CONFIG.GCP_SENDGRID_API_KEY);

exports.sendEmail = function(event){
  const file = event.data;

  const msg = {
    to: CONFIG.GCP_TO_EMAIL,
    from: CONFIG.GCP_FROM_EMAIL,
    subject: 'Fraud was detected!',
    text: `All details can be found in gs://${file.bucket}/${file.name}`,
    html: `All details can be found in <strong>gs://${file.bucket}/${file.name}</strong>`,
  };
    

  sgMail.send(msg).then(function(result){
    console.log(result);
  });
}