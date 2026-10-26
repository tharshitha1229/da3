FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -Dmaven.test.skip=true

EXPOSE 8080

CMD ["sh", "-c", "java -jar target/airline-system-0.0.1-SNAPSHOT.jar"]