## Application details:
1. Java is used for the implementation.
4. Used libraries:
    - slf4j-simple - fast and simple logging
    - Guice - lightweight dependency injection
    - Java spark - a simple web framework
    - jackson - json framework
    - hibernate bean validation - structural validation
    - hibernate - a jpa implementation
    - in-memory-h2  - in memory database
    - Junit - a well-known and simple framework for unit tests
    - Mockito - a simple mocking framework for unit tests

## How to run:
1. Run `mvn clean install`
2. Run 'mvn exec:java'
4. Open `http://localhost:4567/` in your browser, `Application Ready for Use` should be displayed there.
The application is ready for usage.

## Functional Testing:
1. ApplicationIntegrationTest written using simple http client for a money transfer journey

## Api's
1. POST /api/open-account
   request payload - {"name":"ram","balance":1000.50} ,
   response payload - {"accountId" : 54673,""name":"ram","balance":1000.50}
   Success Status - 201;

2. GET /api/accounts/:accountId
   response payload - {"accountId" : 54673,""name":"ram","balance":1000.50}
   Success Status - 201;

3. POST /api/money-transfer
   request payload - {"sourceAccountId":4567, "destinationAccountId": 123456, "transferAmount":1000.50}
   Success Status - 200;

4. POST /api/money-transfer failure
   request payload - {"sourceAccountId":123456, "destinationAccountId": 123456, "transferAmount":1000.50}
   response payload - [{"key" : "accountId", "message" : "same source and destination account not allowed"}]
   failure Status - 400;
