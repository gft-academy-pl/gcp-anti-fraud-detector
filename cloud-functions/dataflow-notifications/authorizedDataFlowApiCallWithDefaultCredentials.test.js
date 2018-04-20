const assert = require('assert');
const {
	google
} = require('googleapis');
const path = require('path');

describe('Dataflow API', function() {


	it('should list all jobs using default application authorization method', async function() {
		const auth = await google.auth.getClient({
			scopes: [
				'https://www.googleapis.com/auth/cloud-platform',
				'https://www.googleapis.com/auth/userinfo.email'
			]
		});
		const projectId = await google.auth.getDefaultProjectId();
		const result = await google.dataflow({
				version: 'v1b3',
				auth: auth
			})
			.projects.jobs.list({
				projectId: projectId
			});

		//then
		console.log('JOB names: ', result.data.jobs.map(el => el.name).join(','));
		assert.ok(result.data.jobs.length > 0);
	})


});