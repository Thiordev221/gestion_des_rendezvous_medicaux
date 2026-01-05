
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY Gestion_des_Rendezvous/pom.xml .
COPY Gestion_des_Rendezvous/src ./src

RUN mvn clean package -DskipTests



FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/Gestion_des_Rendezvous*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
