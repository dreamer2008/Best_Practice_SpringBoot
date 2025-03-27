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

# How to run
## Prerequisites
Clone the repository to your local machine.

## Run locally
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
3. The documentation with Swagger UI is available at the following URL.
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## Run with Docker
1. Install the dependencies
   - [Docker](https://www.docker.com/) (Version: 19.03 or higher)
   - [Docker Compose](https://docs.docker.com/compose/) (Version: 2.5.0 or higher)
2. Run the following command in the terminal
   ```shell
   docker compose up
   ```
3. The documentation with Swagger UI is available at the following URL.
   ```
   http://{$YOUR_HOST}:18080/swagger-ui/index.html
   ```
   
## Run on Cloud Platforms
   Deploy the application on cloud platforms like AWS, Azure, GCP, etc.
   
   * AWS
       - Deployment on EC2 ✅
       - Deployment on ECS ✅
       - Deployment on EKS (TODO)
   - Azure
   - GCP

# Design

1. Layers
    
    1.1 Core Layers

    * Controller Layer
    * Service Layer
    * Repository Layer
    * DTO Layer
    * Model Layer
    * Mapper Layer

    1.2 Other Layers/Packages
    
    * Configuration Layer
    * Exception Layer
    * Utility Layer
    * Validation Layer
 
2. Modeling and Validation
   * Using PO(Persistence Object) and DTO(Data Transfer Object) to separate the data layer and service layer.
   * Using [MapStruct](https://mapstruct.org/) to convert PO to DTO and vice versa.
   * Using Lombok to reduce the boilerplate code.
   * Using Hibernate Validator to validate the request data.

3. Testing
    * Using JUnit 5 and Mockito to write the unit tests.
    * Using Spring Boot Test to write the integration tests.
    