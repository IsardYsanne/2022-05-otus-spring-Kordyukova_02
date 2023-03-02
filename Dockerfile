FROM openjdk:11
ADD /target/library-0.0.1-SNAPSHOT.jar /library-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","/library-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080