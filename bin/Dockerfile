FROM eclipse-temurin:8-jdk

# Cria diretório da aplicação
RUN mkdir /app
WORKDIR /app

# Copia o JAR para dentro do container
COPY target/*.jar app.jar

# Comando para iniciar a aplicação
CMD ["java", "-jar", "app.jar"]
