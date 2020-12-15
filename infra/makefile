SHELL := /bin/bash

build-bacen:
	@ make --directory=bacen build-maven

build-database:
	@ make --directory=database build-maven

build-messaging:
	@ make --directory=messaging build-maven

build: build-bacen build-database build-messaging