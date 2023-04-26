FROM openjdk:17

EXPOSE 9003

ADD /target/Appointment-service.jar Appointment-service.jar

ENTRYPOINT [ "java","-jar","/Appointment-service.jar"]