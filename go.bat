#!/bin/bash

IMAGE_NAME=xiangming/java8:node6;

echo "Pulling latest code...";
git pull --all

if ! docker ps | grep "performance" | grep -v grep; then

  echo "Running docker image...";
  docker run \
    --tty \
    --interactive \
    --volume=$(pwd):/root/Developer/performance \
    --workdir=/root/Developer/performance \
    --rm \
    --name performance \
    $IMAGE_NAME /bin/sh \
    -c "./start_test.sh"
else
  echo "'performance' has already been running.";
fi
