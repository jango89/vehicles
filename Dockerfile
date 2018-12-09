#FROM adoptopenjdk/maven-openjdk8:latest
FROM goyalzz/ubuntu-java-8-maven-docker-image
ADD . /code

WORKDIR /code

EXPOSE 8080

RUN mvn clean install