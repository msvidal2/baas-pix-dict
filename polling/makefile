SHELL := /bin/bash

build-claim:
	@ make --directory=claim build

build-claim-image:
	@ make --directory=claim build-image

build-infraction:
	@ make --directory=infraction build

build-infraction-image:
	@ make --directory=infraction build-image

build-cancel-portability:
	@ make --directory=cancel-portability build

build-cancel-portability-image:
	@ make --directory=cancel-portability build-image

build-overdue-possession-claim:
	@ make --directory=overdue-possession-claim build

build-overdue-possession-claim-image:
	@ make --directory=overdue-possession-claim build-image

build-image:
	@ make --directory=claim build-image
	@ make --directory=infraction build-image
	@ make --directory=cancel-portability build-image
	@ make --directory=overdue-possession-claim build-image

build: build-claim build-infraction build-cancel-portability build-overdue-possession-claim