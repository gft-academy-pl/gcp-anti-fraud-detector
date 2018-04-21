const assert = require('assert');
const index = require('./index');
const {
	google
} = require('googleapis');
const path = require('path');
const fs = require("fs");
const CONFIG = JSON.parse(fs.readFileSync(path.join(__dirname, 'config.json')));

describe('Dataflow API', function() {

	it('should list all jobs using default application authorization method', function(done) {
		google.auth.getClient({
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
					assert.ok(result);
					console.log('result', result);
					return result;
				});
			});
		}).then(res => done(), err => done());
	})


});