SHELL := /bin/bash

build-maven:
	mvn clean install

build-image:
	docker build -t ${GO_PIPELINE_GROUP_NAME}:${TAG}-polling-infraction .

build: build-maven build-image
