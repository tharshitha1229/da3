FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN if [ -f "./mvnw" ]; then \
      chmod +x ./mvnw && ./mvnw clean package -Dmaven.test.skip=true; \
    elif [ -f "./backend/mvnw" ]; then \
      cd backend && chmod +x ./mvnw && ./mvnw clean package -Dmaven.test.skip=true && cp target/*.jar /app/target/ 2>/dev/null || cp target/*.jar /app/; \
    fi

EXPOSE 8080

CMD ["sh", "-c", "JAR_FILE=$(find . -name 'airline-system-*.jar' | grep -v 'original' | head -n 1); java -Dserver.port=${PORT:-8080} -jar ${JAR_FILE:-target/airline-system-0.0.1-SNAPSHOT.jar}"]