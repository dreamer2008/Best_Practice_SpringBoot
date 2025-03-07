FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/springboot-bp.jar springboot-bp.jar
EXPOSE 8080
CMD ["java","-jar","springboot-bp.jar"]