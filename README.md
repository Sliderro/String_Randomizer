# String_Randomizer
String randomizer application created as a recruitment assignment. 
Application allows for creating requests to generate a file containing unique randomized strings with given length using given charset.

## Requirements
* Docker Desktop 4.13.0 or Docker Engine 20.10.20

## Used technologies
* Java 11
  * Maven 3.8.1
  * Spring Boot 2.7.5
* MySQL 8.0.31

## Launching
After cloning repository run `docker-compose up` in the main folder (containing docker-compose.yml file).

## Usage
After launching, the application is running on `localhost:8080`. 

User can operate the application using following HTTP requests:
* GET `/api/jobs` - returns a message with number of jobs currently running.
* POST `/api/jobs` - sends a request for a new job, returns information about the ID of job we requested, requires a body in json with following fields:
  * "min" : Integer - minimum length of generated string,
  * "max" : Integer - maximum length of generated string,
  * "numberOfStrings" : Integer - total number of generated strings in request,
  * "charset" : String - string consisting of desired characters to be used in creating random strings.
 * GET `/api/jobs/get-file/{id}` - downloads a file created by job we requested.
 
## Warning
Using 3GB of RAM memory for java heap resulted in `java.lang.OutOfMemoryError: Java heap space` while requesting a job
that required creating around 30M "short" (around 10 characters long) strings or around 5M "long" (around 150 characters long) strings.

Please take this into consideration and adjust your requests accordingly.
    
