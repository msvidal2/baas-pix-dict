SHELL := /bin/bash

build-maven:
	mvn clean install

build-images: build-maven
	@ make --directory=dict build-image
	@ make --directory=polling build-image
	@ make --directory=worker build-image

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
	