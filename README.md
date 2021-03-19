# fdk-data-transformation-service
A service that transforms our harvested data to make them more suited for doing analytics

## Requirements
- maven
- java 15
- docker
- docker-compose

## Run locally
```
docker-compose up -d
mvn spring-boot:run
```

## Run tests
```
mvn clean verify
```

## Query parameters
`catalog` is required, and the only acceptable values are `datasets`, `dataservices`, `concepts`, `informationmodels`, `publicservices` and `events`
`environment` is required, and the only acceptable values are `staging`, `demo` and `prod`
