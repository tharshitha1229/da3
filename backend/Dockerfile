FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests && cp target/airline-system-*.jar /app/app.jar

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/app.jar app.jar

EXPOSE 8080 10000

CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -Xmx350m -Xms128m -jar app.jar"]