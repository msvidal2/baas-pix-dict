SHELL := /bin/bash

build-maven:
	mvn clean install

build-image:
	docker build -t picpay/baas-pix-dict-polling-infraction .

build: build-maven build-image