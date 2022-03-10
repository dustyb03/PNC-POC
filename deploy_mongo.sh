#!/usr/bin/env bash

# Store mongodb data in local dir
#docker run --name mongodb -d -v ./mongo_data:/data/db mongo

docker run --name mongodb -d \
  -v mongo_data:/data/db \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=password123 \
  -e MONGO_INITDB_ROOT_PASSWORD=username456 mongo