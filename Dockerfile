FROM openjdk:17
COPY build/libs/*T.jar management.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/management.jar"]