# --------------------
# 1. BUILD STAGE (Generar el JAR)
# --------------------
FROM eclipse-temurin:17-jdk-jammy AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo pom.xml y el resto del código fuente
COPY pom.xml .
COPY src /app/src

# Ejecuta el build de Maven. Esto genera el JAR en /app/target/
# 'dependency:go-offline' acelera futuras construcciones
RUN ./mvnw clean package -DskipTests

# --------------------
# 2. RUNNING STAGE (Ejecutar la aplicación)
# --------------------
FROM eclipse-temurin:17-jre-jammy

# Copia el JAR generado en la etapa 'build' al contenedor de producción
# El nombre 'app.jar' es arbitrario
COPY --from=build /app/target/*.jar app.jar

# Define el puerto de la aplicación (por defecto Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]