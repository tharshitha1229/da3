FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY . .

RUN if [ -f "./mvnw" ]; then \
      chmod +x ./mvnw && ./mvnw clean package -DskipTests; \
    elif [ -f "./backend/mvnw" ]; then \
      cd backend && chmod +x ./mvnw && ./mvnw clean package -DskipTests && cp target/*.jar /app/app.jar; \
    fi && \
    if [ ! -f "/app/app.jar" ]; then \
      find . -name "*.jar" ! -name "*original*" -exec cp {} /app/app.jar \; ; \
    fi

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/app.jar app.jar

EXPOSE 8080 10000

CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
