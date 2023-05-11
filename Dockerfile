FROM openjdk:17.0.2
VOLUME /tmp
EXPOSE 10000
COPY maven/target/service.jar service.jar
ENTRYPOINT ["java","-jar","/service.jar"]