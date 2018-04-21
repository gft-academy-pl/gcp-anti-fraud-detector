const assert = require('assert');
const fs = require("fs");
const path = require('path');

describe('Sending e-mail', function() {

	it('should load config.json and contains properties', async function() {
		const CONFIG = JSON.parse(fs.readFileSync(path.join(__dirname, 'config.json')));
		assert.ok(CONFIG.GCP_SENDGRID_API_KEY);
		assert.ok(CONFIG.GCP_FROM_EMAIL);
		assert.ok(CONFIG.GCP_TO_EMAIL);
	})

});