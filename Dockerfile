# BUILD
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia solo el pom.xml para optimizar la caché de dependencias.
COPY pom.xml .

#Descarga todas las dependencias
RUN mvn dependency:go-offline -B


COPY src ./src

# Compila el proyecto y genera el JAR, saltándose los tests.
# Usamos -B para modo batch (no interactivo).¡
RUN mvn clean package -DskipTests -B


#  RUNTIME
FROM eclipse-temurin:21-jre-alpine

ARG FINAL_JAR_NAME=gustos-page-backend-0.0.1-SNAPSHOT.jar
WORKDIR /app

EXPOSE 8080

# Copia el JAR generado de la etapa 'build' a esta nueva imagen
COPY --from=build /app/target/${FINAL_JAR_NAME} app.jar

# ejecutar la aplicación.

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=80"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]