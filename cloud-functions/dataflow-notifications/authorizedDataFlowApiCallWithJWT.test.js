const assert = require('assert');
const index = require('./index');
const {
	google
} = require('googleapis');
const path = require('path');
const fs = require("fs");
const CONFIG = JSON.parse(fs.readFileSync(path.join(__dirname, 'config.json')));

google.auth.getClient({
	keyFile: path.join(__dirname, 'jwt.keys.json'),
	scopes: [
		'https://www.googleapis.com/auth/cloud-platform',
		'https://www.googleapis.com/auth/userinfo.email'
	]
}).then(auth => {
	return google.auth.getDefaultProjectId().then(projectId => {
		console.log('ProjectId', projectId);
		return index.createJob(auth, projectId, {
			bucket: CONFIG.INPUT_BUCKET,
			name: 'trades-small.csv'
		}).then(result => {
			assert.equal(result.data.type, 'JOB_TYPE_BATCH');
			console.log('result', result.data);
		}, err => console.log(err));
	});
});
