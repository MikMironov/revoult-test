App can be compiled with

`maven clean compile package`

After that, you can run

`java -jar revoult-test-1.0-SNAPSHOT-jar-with-dependencies.jar`

App starts on localhost:8080, API:


`GET http://localhost:8080/accounting/{id}`

`POST http://localhost:8080/accounting/`
body - Account entity (id and balance). Id will be generated.

`PUT http://localhost:8080/accounting/{source_id}/{target_id}/{amount}`

where

`source_id - source account ID`
`target_id - target account ID`
`amount - amount of money to transfer` (no currency)

Application is use embedded H2 database and not save for production