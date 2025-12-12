# --------------------
# 1. BUILD STAGE (Generar el JAR)
# --------------------
FROM eclipse-temurin:17-jdk-jammy AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia TODOS los archivos del repositorio (pom.xml, src, mvnw, .mvn)
# Esto garantiza que el build de Maven tiene todo lo que necesita.
COPY . .

# Asegúrate de que el script mvnw sea ejecutable (es un paso clave)
RUN chmod +x mvnw

# Ejecuta el build de Maven. Esto genera el JAR en /app/target/
# Se omite la ejecución de tests con -DskipTests
RUN ./mvnw clean package -DskipTests

# --------------------
# 2. RUNNING STAGE (Ejecutar la aplicación)
# --------------------
# Usa la imagen más ligera (solo JRE) para producción
FROM eclipse-temurin:17-jre-jammy

# Copia el JAR generado en la etapa 'build' al contenedor de producción
COPY --from=build /app/target/admin-panel-backend.jar app.jar

# Define el puerto de la aplicación (por defecto Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]