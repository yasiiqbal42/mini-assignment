Mini Assignment to create backend for IMDB Movie using Dynamo DB.

Framework - Spring-boot
Database - Dynamo DB

Steps to run this application.

1- Install the Dynamo DB Locally.

access this URL for installation - https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html

Run DynamoDB
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb

Also Install AWS CLI 

Commands :- 
aws configure
Aws access Key ID: key1
AWS secret key ID: key2


Show Tables
aws dynamodb list-tables --endpoint-url http://localhost:8000

Create Table
aws dynamodb create-table --attribute-definitions AttributeName=id,AttributeType=S --table-name Movie --key-schema AttributeName=id,KeyType=HASH --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 --region us-east-1 --output json --endpoint-url http://localhost:8000

Server Port: 8081

Step 2- Run the Application (Server Port: 9090)

Use Postman or any other tool to hit the API endpoints as (localhost:9090/movie/)
Pass the X-Auth-Token in the header and for authorization use "Basic Auth" under Authorization section
Basic Auth ----------
user name : yasir
password : yasir

Endpoints Suggestions :
localhost:9090/movie
localhost:9090/movie/userReview/4
localhost:9090/movie/director/Allan Dwan
localhost:9090/movie/upload

Description

Run application.proerties file to execute

Endpoint Description

Use port 8081 for accessing the apis and using endpoint /movie for all the apis.