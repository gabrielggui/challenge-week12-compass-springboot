# Challenge 3
## Objective
The objective of this project is to create an application that asynchronously fetches posts from an external API, enriches them with comment data, and logs processing updates. Clients will be able to retrieve posts and access the processing history through the real-time API. For the development of the API, we have utilized the Java programming language in conjunction with the Spring Boot framework and the H2 database. Additionally, we have incorporated other relevant dependencies for specific purposes.

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
