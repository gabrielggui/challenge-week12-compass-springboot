# Challenge 3
## Objective
Create an application that asynchronously fetches posts from an external API, enriches them with comment data, and keeps a log of processing updates. The client will then be able to search for posts and the history of states through the API immediately. For the development of the API, the Java programming language will be used in conjunction with the Spring Boot framework and the H2 database. Additionally, other relevant dependencies will be incorporated for specific purposes.

## Implemented features
* CRUD (Create, Read, Update, Delete, Patch)
* Messaging (RabbitMQ)
* OpenAPI (using Swagger)
* ProblemDetails [(RFC 7807)](https://datatracker.ietf.org/doc/html/rfc7807)
* Caching
* Pagination
* Logging (SLF4J)

## Prerequisites
Before you begin, ensure that you have Docker installed on your machine. You can download and install Docker from [here](https://www.docker.com/products/docker-desktop).

### Start RabbitMQ Server

Open your terminal and run the following command to start the temporary RabbitMQ server using Docker:

```bash
docker run --rm -it -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest rabbitmq:management
```
The RabbitMQ management console will be accessible at http://localhost:15672/. You can log in using the provided credentials (guest/guest).

## API documentation
To access the API documentation, you need to start the application and then access the specified address: http://localhost:8080/. This route will provide an interactive interface that outlines all pertinent information about the API, encompassing available endpoints, accepted parameters, and response formats.
