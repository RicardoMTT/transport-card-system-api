# Etapa de build
# El resultado de esta etapa es un archivo JAR que se genera
# en el directorio /app/target dentro del contenedor
# Este JAR se copiara a la imagen final en la siguiente etapa (Etapa de runtime)
# En esta etapa el contenedor tendra una carpeta /app y dentro de ella el archivo JAR
# tambien tendra el archivo mvnw que es el script de maven , y el archivo pom.xml que es el archivo de configuracion de maven
# tambien tendra el archivo application.yml que es el archivo de configuracion de springboot
# tambien tendra el archivo src que es el codigo fuente de la aplicacion
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests # se ejecuta en el directorio /app

# Etapa de runtime
# El resultado de esta etapa es la imagen final que se ejecutara
# en el contenedor final que se usara para desplegar la aplicacion
# Esta imagen contiene el JAR generado en la etapa de build
# y los comandos para ejecutarlo en el contenedor final
# La imagen final solo contendra la imagen base con jre, tambien dentro del directorio app el JAR generado
# , la variable de entorno PORT y los comandos para ejecutarlo
# No contendra la imagen base con jdk ni la etapa de build, ni el codigo fuente
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV PORT=10000
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar /app/app.jar"]