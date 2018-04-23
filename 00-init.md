![GCP Logo](https://raw.githubusercontent.com/gft-academy-pl/gcp-anti-fraud-detector/master/assets/google-cloud-platform.png)

## Agenda

- Introduction to GCP
- Introduction to GCP shell and editor
- Project overview
- Initialization / setup

## Introduction to GCP

Google Cloud Platform is a public cloud solution providing a set of product: compute, storage, database cloud AI, networking, identity & security. 

https://cloud.google.com/ 
http://console.cloud.google.com

## Introduction to GCP shell and editor

### Cloud shell

Google Cloud Shell provides you with command-line access to your cloud resources directly from your browser. You can easily manage your projects and resources without having to install the Google Cloud SDK or other tools on your system. With Cloud Shell, the Cloud SDK gcloud command-line tool and other utilities you need are always available, up to date and fully authenticated when you need them.

hen you start Cloud Shell, g1-small Google Compute Engine virtual machine running a Debian-based Linux operating system is provisioned for you. Cloud Shell instances are provisioned on a per-user, per-session basis. The instance persists while your Cloud Shell session is active and terminates after a hour of inactivity.

### Cloud shell editor

```
cloudshell edit filename
```

## Project overview

![Diagram](https://github.com/gft-academy-pl/gcp-anti-fraud-detector/blob/master/assets/GFT%20Academy%20-%20anti%20fraud%20detector.png?raw=true)

## Initialization / setup

**Create dedicated project**

In order to easier clean resources create dedicated project and switch to it after creation.

**Clean up**

After the workshop is over, please delete the project as your acount will be still billed for the bucket usage.

**Clone repository**

```
cd ~
git clone https://github.com/gft-academy-pl/gcp-anti-fraud-detector.git
cd gcp-anti-fraud-detector
ls
```

## Navigation

- [Next Step](./01-storage.md)
