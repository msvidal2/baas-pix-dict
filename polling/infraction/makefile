SHELL := /bin/bash

build-maven:
	mvn clean install

build-image:
	docker build -t ${GO_PIPELINE_GROUP_NAME}:${TAG}-polling-infraction .
	docker tag ${GO_PIPELINE_GROUP_NAME}:${TAG}-polling-infraction 493959330548.dkr.ecr.us-east-1.amazonaws.com/${GO_PIPELINE_GROUP_NAME}:${TAG}-polling-infraction
	docker push 493959330548.dkr.ecr.us-east-1.amazonaws.com/${GO_PIPELINE_GROUP_NAME}:${TAG}-polling-infraction

build: build-maven build-image
