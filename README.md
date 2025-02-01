# Built with
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL](https://www.mysql.com/)
- [Apache Maven](https://maven.apache.org/)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Lombok](https://projectlombok.org/)
- [MapStruct](https://mapstruct.org/)
- [Swagger](https://swagger.io/)
- [Spring Doc Open API](https://springdoc.org/)

# Prerequisites
Clone the repository to your local machine.

# Run locally
1. Install the dependencies
   - [Java](https://www.oracle.com/java/technologies/downloads/#java21)
   - [MySQL](https://www.mysql.com/) (Version: 8.0 or higher)
     - Create the user and database in MySQL as per `application.properties`
     ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/platform
        spring.datasource.username=root
        spring.datasource.password=1qazxsw2
     ```
2. Import the project in your IDE(e.g. IntelliJ IDEA) and Run the application 
3. Test the endpoints at the following URL or use Postman
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

# Run with Docker
1. Install the dependencies
   - [Docker](https://www.docker.com/) (Version: 19.03 or higher)
   - [Docker Compose](https://docs.docker.com/compose/) (Version: 2.5.0 or higher)
2. Run the following command in the terminal
   ```shell
   docker compose up
   ```
3. Test the endpoints at the following URL or use Postman
   ```
   http://localhost:18080/swagger-ui/index.html
   ```