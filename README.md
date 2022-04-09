# simple-store-app

This is a Spring Boot RESTful service initialized at https://start.spring.io, written in Java 11 and built with Maven.

The REST API is documented for convenience with OpenAPI and can be viewed through Swagger here
http://localhost:8080/swagger-ui/index.html

### Brief Project Description

A product that can be sold to customers is composed of different smaller component articles.

#### Comments

The requirements were not very clear. \
The assignment description mentions product price but the inventory doesn't declare this data.

### Installation and Running

Run the command `mvnw clean verify` to build and test. \
From the command line, you may start the application like: `mvn spring-boot:run`

### Implementation Details

* Flyway to manage the DB model on H2 Database.
* Spring Data JPA to abstract the connection to the DB and the and data model.
* OpenAPI and Swagger to describe the REST API.
* Spring and Java Bean validation to validate the input.
* Lombok to work around the verbosity of Java.
* JUnit 5 for testing, and AssertJ and Hamcrest for assertions.
* Domain package structure as advised by Spring docs: 
https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/html/using-boot-structuring-your-code.html

### TO DO...

* Profiles: development, staging, production.
* Security: authorize access to Admin endpoints and authenticate users who buy products.
* Caching: cache a subset of the inventory in memory.
* Cloud: use Postgres instead of H2 through test containers for consistency.
* Containerization: package the application in a container image.
* Error Handling: handle input parsing errors properly with ExceptionHandler.
* More Unit Tests.

### Known Issues

* Transaction isolation can be optimized

---
_Engineered and developed by Raafat_
