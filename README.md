# template-processor
Create dynamic templates using ApachePOI

This is a POC to test the ApachePOI library. 
You upload a doc/docx file and then you can reuse it as many times as you want with different values.


The template is stored in a MySQL database (you can configure it at application.yml).
The database migrations are done using liquibase, so if you want to modify anything, do it the liquibase way. You can check the changelogs at master.xml in the resources folder.

The template processing is done using the Apache POI Library. 

The API is documented using SwaggerUI.

USAGE

run the command: mvn springboot:run

open localhost:8080/swagger-ui.html

Upload your doc/docx file at the endpoint /api/template/create and give it a name. Your template data will be stored in the MySQL database configured in your application-<profile>.yml file.

To download your template with the variables switched by real values, use the endpoint /api/template/print/{templateName} and in the request body, put your pairs in JSON format.

Example: 
[
	{"name": "FIRST_NAME", "value": "Carlos Max"},
	{"name": "IN_DATE", "value": "10/05/2021"}
]

The document will be printed and all the words that matches with the "name" attributes will be replaced by its value.



