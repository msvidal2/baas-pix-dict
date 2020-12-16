SHELL := /bin/bash

build-claim:
	@ make --directory=claim build

build-claim-image:
	@ make --directory=claim build-image

build-infraction:
	@ make --directory=infraction build

build-infraction-image:
	@ make --directory=infraction build-image

build-image:
	@ make --directory=claim build-image
	@ make --directory=infraction build-image

build: build-claim build-infraction