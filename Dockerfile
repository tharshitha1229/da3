FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY . .

RUN if [ -f "pom.xml" ]; then \
      mvn clean package -DskipTests && \
      (cp target/airline-system-*.jar /app/app.jar 2>/dev/null || cp backend/target/airline-system-*.jar /app/app.jar); \
    elif [ -f "backend/pom.xml" ]; then \
      cd backend && mvn clean package -DskipTests && cp target/airline-system-*.jar /app/app.jar; \
    fi

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/app.jar app.jar

EXPOSE 8080 10000

CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -Xmx350m -Xms128m -jar app.jar"]
