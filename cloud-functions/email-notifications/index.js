const {
	google
} = require('googleapis');
const fs = require("fs");
const path = require('path');

const CONFIG = JSON.parse(fs.readFileSync(path.join(__dirname, 'config.json')));

exports.triggerDataflowFn = (event) => {

	console.log('Processing incomming event');

	const file = event.data;

	return google.auth.getClient({
		keyFile: path.join(__dirname, 'jwt.keys.json'),
		scopes: [
			'https://www.googleapis.com/auth/cloud-platform',
			'https://www.googleapis.com/auth/userinfo.email'
		]
	}).then((auth) => {
		return google.auth.getDefaultProjectId()
			.then((projectId) => {
				console.log(`Succesfully authorized with JWT key, project: ${projectId}`);

				return google.dataflow({
						version: 'v1b3',
						auth: auth
					})
					.projects.templates.create({
						projectId: projectId,
						resource: {
							parameters: {
								inputFile: `gs://${file.bucket}/${file.name}`,
								output: `${CONFIG.OUTPUT_BUCKET}/frauds-${file.name}`
							},
							jobName: 'fraud-detector-input-data-triggering-dataflow-' + new Date().toISOString(),
							gcsPath: CONFIG.GS_PATH
						}
					}).then((result) => {
						
						console.log('Result: ', result);
						return result;
						
					}, (err) => console.log('Exception occurred while executing DataFlow job template', err.message));

			}, (err) => console.log('Exception occurred while obtaining default projectId', err.message));

	}, (err) => console.log('Exception occurred while obtaining credentials', err.message));

};