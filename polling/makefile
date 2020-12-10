SHELL := /bin/bash

build-maven:
	mvn clean install

build-image:
	docker build -t picpay/baas-pix-dict-bacen-pulling .

build:
	build-maven
	build-image