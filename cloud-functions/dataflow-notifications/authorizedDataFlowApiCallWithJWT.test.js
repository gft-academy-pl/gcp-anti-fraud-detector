const index = require('./index');
const {
	google
} = require('googleapis');
const path = require('path');
const fs = require("fs");
const CONFIG = JSON.parse(fs.readFileSync(path.join(__dirname, 'config.json')));

describe('Dataflow API', function() {

	it('should list all jobs using JWT', async function() {
		const auth = await google.auth.getClient({
			keyFile: path.join(__dirname, 'jwt.keys.json'),
			scopes: [
				'https://www.googleapis.com/auth/cloud-platform',
				'https://www.googleapis.com/auth/userinfo.email'
			]
		});
		console.log('auth', auth);
		const projectId = await google.auth.getDefaultProjectId();
		const result = await index.createJob(auth, projectId, {
			bucket: CONFIG.INPUT_BUCKET,
			name: 'trades-small.csv'
		});
		console.log('Result: ', result.data);
	})

});