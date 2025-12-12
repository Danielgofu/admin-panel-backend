# --------------------
# 1. BUILD STAGE (Generar el JAR)
# --------------------
FROM eclipse-temurin:17-jdk-jammy AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos del Maven Wrapper (mvnw, mvnw.cmd y .mvn/)
COPY mvnw .
COPY .mvn .mvn

# Copia el archivo pom.xml y el resto del código fuente
COPY pom.xml .
COPY src /app/src

# Asegúrate de que el script mvnw sea ejecutable (es un paso clave)
RUN chmod +x mvnw

# Ejecuta el build de Maven. Esto genera el JAR en /app/target/
RUN ./mvnw clean package -DskipTests

# --------------------
# 2. RUNNING STAGE (Ejecutar la aplicación)
# --------------------
FROM eclipse-temurin:17-jre-jammy

# Copia el JAR generado en la etapa 'build'
COPY --from=build /app/target/*.jar app.jar

# Define el puerto de la aplicación (por defecto Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]