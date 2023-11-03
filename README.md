# Posts API
## Objective
Challenge proposed by the Compass UOL internship program, whose objective was to create an application that asynchronously fetches posts from an external API, enriches them with comment data, and logs processing updates. In this application, clients will be able to retrieve the posts and access the processing history through the real-time API. For the development of the API, the Java programming language was used in conjunction with the Spring Boot framework and the H2 database. In addition, other relevant dependencies were incorporated for specific purposes.

## Implemented resources
### v1 (20230831)
* CRUD (Create, Read, Update, Delete)
* ~~Messaging (RabbitMQ)~~ (replaced by embedded ActiveMQ in **v2** Update)
* OpenAPI (using Swagger)
* ProblemDetails [(RFC 7807)](https://datatracker.ietf.org/doc/html/rfc7807)
* Caching
* Pagination
* Logging (SLF4J)

### v2 (20230901)
* Replacement: RabbitMQ -> embedded ActiveMQ

### v3 (20230902)
* Fixed bugs in data persistence
* Optimizing performance

## API documentation
To access the API documentation, you need to start the application and then access the specified address: http://localhost:8080/. This route will provide an interactive interface that outlines all pertinent information about the API, encompassing available endpoints, accepted parameters, and response formats.
