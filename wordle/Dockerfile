# the first stage of our build will use a maven 3.6.1 parent image
FROM maven:3.9.0-amazoncorretto-11 AS MAVEN_BUILD
# copy the pom and src code to the container
COPY ./ ./
# package our application code
RUN mvn clean package


# we will use openjdk 8 with alpine as it is a very small linux distro
FROM amazoncorretto:11-alpine3.14
# copy the packaged jar file into our docker image
#COPY target/wordle-0.0.1-SNAPSHOT.jar /wordle.jar
COPY --from=MAVEN_BUILD /target/wordle-0.0.1-SNAPSHOT.jar /wordle.jar

# set the startup command to execute the jar
CMD ["java", "-jar", "/wordle.jar"]