SHELL := /bin/bash

APPLICATION?=maven

build-maven:
	@ mvn clean install

build-push-images: build-$(APPLICATION)
	docker tag ${GO_PIPELINE_GROUP_NAME}:${TAG} 493959330548.dkr.ecr.us-east-1.amazonaws.com/${GO_PIPELINE_GROUP_NAME}:${TAG}
	docker push 493959330548.dkr.ecr.us-east-1.amazonaws.com/${GO_PIPELINE_GROUP_NAME}:${TAG}

build-dict:
	@ mvn --projects dict -am clean install
	@ make --directory=dict build-image

build-polling-claim:
	@ mvn --projects com.picpay.banking.pix.dict.polling:claim -am clean install
	@ make --directory=polling build-claim-image

build-polling-infraction:
	@ mvn --projects com.picpay.banking.pix.dict.polling:bacen-infraction-task -am clean install
	@ make --directory=polling build-infraction-image

build-polling-cancel-portability:
	@ mvn --projects com.picpay.banking.pix.dict.polling:cancel-portability -am clean install
	@ make --directory=polling build-cancel-portability-image

build-polling-overdue-possession-claim:
	@ mvn --projects com.picpay.banking.pix.dict.polling:overdue-possession-claim -am clean install
	@ make --directory=polling build-overdue-possession-claim-image

build-worker:
	@ mvn --projects worker -am clean install
	@ make --directory=worker build-image

build-sync:
	@ mvn --projects com.picpay.banking.pix.dict.sync:cid-files -am clean install
	@ make --directory=sync/cid-files build-image

build-sync-cid-events:
	@ mvn --projects com.picpay.banking.pix.dict.sync:cid-events -am clean install
	@ make --directory=sync/cid-events build-image

build-sync-verifier:
	@ mvn --projects com.picpay.banking.pix.dict.sync:sync-verifier -am clean install
	@ make --directory=sync/sync-verifier build-image
