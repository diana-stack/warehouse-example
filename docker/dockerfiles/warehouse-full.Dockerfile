FROM openjdk:11-jre-slim
COPY target/warehouse-full-0.0.1-SNAPSHOT.jar /app/app.jar
ENV CATALINA_OPTS="-Xmx8G"
CMD ["java", "-Xmx8G", "-jar", "/app/app.jar"]
EXPOSE 8007
