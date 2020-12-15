SHELL := /bin/bash

build-claim:
	@ make --directory=claim build

build-infraction:
	@ make --directory=infraction build

build-image:
	@ make --directory=claim build-image
	@ make --directory=infraction build-image

build: build-claim build-infraction