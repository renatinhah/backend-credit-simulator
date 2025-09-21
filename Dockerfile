# Stage 1: Build do app usando Maven
FROM maven:3.9.0-eclipse-temurin-17 AS build

WORKDIR /app

# Copia só os arquivos de dependências para aproveitar cache do Docker
COPY pom.xml .

# Baixa as dependências (cache)
RUN mvn dependency:go-offline

# Copia o código fonte
COPY src ./src

# Builda o jar sem testes (pode rodar testes se preferir)
RUN mvn clean package -DskipTests

# Stage 2: Runtime leve com JRE Alpine
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o jar gerado no stage de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# JVM otimizada para container + start app
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
