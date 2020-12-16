SHELL := /bin/bash

APPLICATION?=maven

build-maven:
	@ mvn clean install

build-images: build-maven
	@ make --directory=dict build-image
	@ make --directory=polling build-image
	@ make --directory=worker build-image

build-push-images: build-$(APPLICATION)
	@ docker tag ${GO_PIPELINE_GROUP_NAME}:${TAG}-dict 493959330548.dkr.ecr.us-east-1.amazonaws.com/${GO_PIPELINE_GROUP_NAME}:${TAG}-dict
	@ docker push 493959330548.dkr.ecr.us-east-1.amazonaws.com/${GO_PIPELINE_GROUP_NAME}:${TAG}-dict

build-core:
	@ make --directory=core build-maven

build-infra: build-core
	@ make --directory=infra build

build-dict: build-infra
	@ make --directory=dict build

build-polling-claim: build-infra
	@ make --directory=polling build-claim

build-polling-infraction: build-infra
	@ make --directory=polling build-infraction

build-polling: build-infra
	@ make --directory=polling build

build-worker: build-infra
	@ make --directory=worker build

build-sync: build-infra
	@ make --directory=sync/cid-files build