# fdk-data-transformation-service
A service (implemented as function) that transforms our harvested data to make them more suited for doing analytics

## Requirements
- maven
- java 15
- docker
- docker-compose

## Run locally
```
mvn function:run
```

## Query parameters
`catalog` is required and the only acceptable values are `datasets`, `dataservices`, `concepts`, `informationmodels`, `publicservices` and `events`
