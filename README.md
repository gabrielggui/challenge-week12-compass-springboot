# Challenge 3
## Objective
Create an application that asynchronously fetches posts from an external API, enriches them with comment data, and logs processing updates. The client will then be able to search for posts and access the history of states through the API in real-time. For the development of the API, we will use the Java programming language in conjunction with the Spring Boot framework and the H2 database. Additionally, we will incorporate other relevant dependencies for specific purposes.

## Implemented features
### v1 (20230831)
* CRUD (Create, Read, Update, Delete, Patch)
* ~~Messaging (RabbitMQ)~~ (replaced by embedded ActiveMQ in **v2** Update)
* OpenAPI (using Swagger)
* ProblemDetails [(RFC 7807)](https://datatracker.ietf.org/doc/html/rfc7807)
* Caching
* Pagination
* Logging (SLF4J)

### v2 (20230901)
* Replaced RabbitMQ for embedded ActiveMQ
* Code refactoring

## API documentation
To access the API documentation, you need to start the application and then access the specified address: http://localhost:8080/. This route will provide an interactive interface that outlines all pertinent information about the API, encompassing available endpoints, accepted parameters, and response formats.
