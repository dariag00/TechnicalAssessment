In order to create the docker image of the service, the following maven goal shall be executed over account-service:
mvn clean spring-boot:build-image
Command to execute the docker image:
docker run -p 9090:9090 docker.io/bsd/tech-account-service:1.0-SNAPSHOT

A postman collection was created to make the testing and revision easier. The collection can be found on /metadata. There is a
collection and an environment. Both are needed, even though the environment only contains a base url param.

The following endpoints are exposed:

GET /accounts -> List of accounts
GET /accounts/id -> Get details of an account
POST /accounts -> Creates an account

POST /transfers -> Accepts a transfer

I would add the additional features in case of being a real feature:
- Redis: in order to cache the requests and save some processing time
- PostgressSQL: The transactions performed in this project need an ACID compliant database. Postgress fullfills that requirement.

I would create an additional docker compose with the dependencies found above and also:
- PGAdmin: Admin interface to check Postgress