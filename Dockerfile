FROM java:8

ADD target/wbccad-integration-0.0.1-SNAPSHOT.jar /opt/wbccad-integration/

EXPOSE 8080

WORKDIR /opt/wbccad-integration/

CMD ["java", "-Xms128m", "-Xmx1g", "-jar", "wbccad-integration-0.0.1-SNAPSHOT.jar"]