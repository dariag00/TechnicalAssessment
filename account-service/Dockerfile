FROM openjdk:11
ADD account-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.secuyarity.egd=file:/dev/./urandom","-Dspring.profiles.active=dev","-jar","/app.jar"]