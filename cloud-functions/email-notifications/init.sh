#!/bin/bash

sed -i 's/__GCP_SENDGRID_API_KEY__/'"$GCP_SENDGRID_API_KEY"'/g' config.json
sed -i 's/__GCP_FROM_EMAIL__/'"$GCP_FROM_EMAIL"'/g' config.json
sed -i 's/__GCP_TO_EMAIL__/'"$GCP_TO_EMAIL"'/g' config.json