#!/bin/sh

echo "Stopping and removing the back-end container"

docker stop  spring-demo-integration

docker rm  spring-demo-integration

echo "Removing the image for the back-end container"
docker image rm sistemedistribuite-spring-demo-integration

echo "Removing the volume for the back-end container"
docker volume rm  sistemedistribuite_spring-demo-integration


echo "Stopping and removing the front-end container"

docker stop energy-visualization-platform-integration

docker rm energy-visualization-platform-integration

echo "Removing the image for the front-end container"
docker image rm sistemedistribuite-energy-visualization-platform-integration

echo "Stopping and removing the MySql container"

docker stop  mysql-integration

docker rm  mysql-integration

echo "Removing the image for the back-end container"
docker image rm mysql

echo "Removing the volume for the back-end container"
docker volume rm  sistemedistribuite_mysql-integration


docker-compose up --build