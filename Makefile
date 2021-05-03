app      = my-app

.PHONY: build
build:
	docker build \
		--target=resolved-dependencies \
		-t "$(app):resolved-dependencies" .

.PHONY: start-console
start-console: build
	docker-compose run --service-ports app /bin/bash

.PHONY: down
down:
	docker-compose down
