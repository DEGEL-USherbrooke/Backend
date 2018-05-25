#!/bin/bash

./gradlew build
docker build -t s6igitlab.gel.usherbrooke.ca:4567/degel/degel-backend:latest .
docker-compose up -d