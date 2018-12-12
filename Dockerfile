#FROM adoptopenjdk/maven-openjdk8:latest
FROM goyalzz/ubuntu-java-8-maven-docker-image
ADD . /code

WORKDIR /code

EXPOSE 8080 6379 27017

RUN mvn clean install