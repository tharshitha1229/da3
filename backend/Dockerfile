FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests && cp target/airline-system-*.jar /app/app.jar

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/app.jar app.jar

EXPOSE 8080 10000

CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]