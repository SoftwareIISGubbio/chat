#!/bin/bash

cd workspace-eclipse/serverchat
mvn clean
mvn package
mv target/serverchat.war ../../apache-tomcat-10.0/webapps
cd ../..
tail -f apache-tomcat-10.0/logs/catalina.out
