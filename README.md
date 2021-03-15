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

## Headers
Content-Type is required and should include charset, i.e: text/turtle;charset=UTF-8

Accept defaults to text/turtle and will always use charset=UTF-8
