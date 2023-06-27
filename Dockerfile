#FROM maven:3.8.6-eclipse-temurin-11-alpine
FROM maven:3.8.6-jdk-11

WORKDIR /application

COPY be ./
COPY fe /fe

RUN apt-get install -y curl
RUN curl -sL https://deb.nodesource.com/setup_16.x | bash - 
RUN apt-get install -y nodejs
#RUN curl -L https://www.npmjs.com/install.sh | sh

RUN npm install -g @angular/cli@14.2.10

WORKDIR /fe
RUN npm install
RUN ng build --configuration production --aot

WORKDIR /fe/dist
RUN cp -a /fe/dist/$(ls -d */|head -n 1). /application/src/main/resources/webapp/


WORKDIR /application
COPY assets ./src/main/resources/webapp


RUN mvn clean package


ENV ASSETS_DIR_NAME=../javache_assets 


WORKDIR /
CMD java -jar /application/target/*.jar

