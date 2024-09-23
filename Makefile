.PHONY: build-dev build run stop test lint eslint prettier buildx-dev buildx help

build:
	docker build --progress=plain --no-cache -t community-center:latest .

build-dev:
	docker build --progress=plain --no-cache -t community-center:latest -f Dockerfile.dev .

buildx:
	docker buildx build --platform linux/amd64,linux/arm64 --progress=plain --no-cache -t community-center:latest .

buildx-dev:
	docker buildx build --platform linux/amd64,linux/arm64 --progress=plain --no-cache -t community-center:latest -f Dockerfile.dev .

run:
	docker-compose up -d

run-dev:
	docker-compose -f docker-compose.dev.yml up -d

stop:
	docker-compose down

stop-dev:
	docker-compose -f docker-compose.dev.yml down

help:
	@echo "-----------------------HELP----------------------"
	@echo "build:  Builds the Docker image using the production Dockerfile."
	@echo "build-dev:  Builds the Docker image using the development Dockerfile."
	@echo "buildx:  Builds the Docker image for multiple platforms using Docker Buildx the production Dockerfile.."
	@echo "buildx:  Builds the Docker image for multiple platforms using Docker Buildx the development Dockerfile.."
	@echo "run:  Starts the Docker container."
	@echo "run-dev:  Starts the Docker dev container."
	@echo "stop:  Stops the Docker container."
	@echo "stop:  Stops the Docker dev container."
	@echo "help:  Show the help menu."
	@echo "-------------------------------------------------"
