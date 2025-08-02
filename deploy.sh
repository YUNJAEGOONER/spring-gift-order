#!/bin/sh

PROJECT_PATH=/home/ubuntu/spring
PROJECT_NAME=spring-gift-order
PORT=8080
BUILD_PATH=build/libs
JAR_NAME=spring-gift-0.0.1-SNAPSHOT.jar

echo "\n change directory where my project is located\n"

cd $PROJECT_PATH/$PROJECT_NAME

echo "update the code from my GITHUB\n"

git pull origin step3

export KAKAO_CLIENT_ID="26873f276e6fdac07b7aaf2f802d711c"
export KAKAO_REDIRECT_URI="http://store.devyunjae.com/kakao-login"
export JWT_SECRET="ComeOnYouGunnersNorthLondonisRedNorthLondonFOREVER"

echo "build my project\n"

./gradlew clean build

PID=$(lsof -i :$PORT -t)

if [ -z $PID]; then
        echo "execute yjshop\n"
else
        echo "kill the process using 8080 before yjshop"
        kill -15 $PID
fi

nohup java -jar $PROJECT_PATH/$PROJECT_NAME/$BUILD_PATH/$JAR_NAME &