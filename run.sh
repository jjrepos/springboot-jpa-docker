#!/usr/bin/env bash
export SERVICE=spring-jpa-docker
export IMAGE_VERSION=latest;
docker stack rm "$SERVICE";
mvn -U clean install -D maven.test.skip=true;
result=$?
if [ "$result" -eq 0 ]; then
  echo "Build was successful, deploying...."
  docker build --rm -t jjrepos.com/microservices/"$SERVICE:$IMAGE_VERSION" .
  docker stack deploy -c deploy/local/stack.yml "$SERVICE"
  docker service logs -f "$SERVICE"_api
else
  echo "Build & deploy failed"
fi

