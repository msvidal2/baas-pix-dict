SHELL := /bin/bash

APPLICATION?=maven

build-maven:
	@ mvn clean install

build-push-images: build-$(APPLICATION)
	@ docker tag ${GO_PIPELINE_GROUP_NAME}:${TAG}-${APPLICATION} 493959330548.dkr.ecr.us-east-1.amazonaws.com/${GO_PIPELINE_GROUP_NAME}:${TAG}-${APPLICATION}
	@ docker push 493959330548.dkr.ecr.us-east-1.amazonaws.com/${GO_PIPELINE_GROUP_NAME}:${TAG}-${APPLICATION}

build-dict:
	@ mvn --projects dict -am clean install
	@ make --directory=dict build-image

build-polling-claim:
	@ mvn --projects com.picpay.banking.pix.dict.polling:claim -am clean install
	@ make --directory=polling build-claim-image

build-polling-infraction:
	@ mvn --projects com.picpay.banking.pix.dict.polling:bacen-infraction-task -am clean install
	@ make --directory=polling build-infraction-image

build-worker:
	@ mvn --projects worker -am clean install
	@ make --directory=worker build-image

build-sync:
	@ mvn --projects com.picpay.banking.pix.dict:cid-files -am clean install
	@ make --directory=sync/cid-files build-image