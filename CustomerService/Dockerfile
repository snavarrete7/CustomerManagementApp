FROM openjdk:17-alpine

WORKDIR /app

COPY . .

RUN ./gradlew bootJar

COPY build/libs/CustomerService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]