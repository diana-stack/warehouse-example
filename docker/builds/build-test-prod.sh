#!/bin/bash
echo "Budowanie warehouse-full"
cd ../..
mvn clean package -P test-prod -DskipTests=true
docker image build --tag warehouse-full --file ".\docker\dockerfiles\warehouse-full.Dockerfile" .
docker save -o ./docker/image/warehouse-full.tar warehouse-full
#cd ..

echo "Koniec budowania"
read
