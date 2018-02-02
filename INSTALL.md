TODO:

- Docker CE setup from https://www.docker.com/community-edition

Docker
- ./mvnw install dockerfile:build
- docker run -p 8080:8080 -m 64M --rm --name node1 -t csc/bdse
- docker run -p 8080:8081 -t csc/bdse
- TODO testcontainers


To start:
- run NodeApplication from IDE
- open browser and test app started at http://localhost:8080/keys
- run data loading by NodeApplication-scenario.sh script