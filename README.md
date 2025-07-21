# AtlasValue

REST API that allows to manage family tree. 

## How to run:
- Pre-installation of Maven and jdk17 is required
- Clone the Git repository: `https://github.com/PavelBui/FamilyTree` (main branch)
- Run the command `mvn clean package` in the root folder
- Run the command `java -jar TaskTrackTemplate-1.0-SNAPSHOT.jar` in the `target` folder

## Parameters
- **Port** - 8082
- **URL** - http://localhost:8082
- **Swagger** - http://localhost:8082/swagger-ui/index.html

## Person endpoints
- **Create person** - POST request http://localhost:8082/person
    - `RequestBody: PersonDto`
    - `ResponseBody: PersonDto`
- **Get person** - GET request http://localhost:8082/person/{id}
    - `PathVariable: person id`
    - `ResponseBody: PersonDto`
- **Get all persons** - GET request http://localhost:8082/person
    - `ResponseBody: List of PersonDto`
- **Update person** - PUT request http://localhost:8082/person/{id}
    - `PathVariable: person id`
    - `RequestBody: PersonDto`
    - `ResponseBody: PersonDto`
- **Delete person** - DELETE request http://localhost:8082/person/{id}
    - `PathVariable: person id`
    - `ResponseBody: String` (Person was deleted successfully)

## PersonDto (example)
```json
{
    "id": 0,
    "title": "string",
    "time_period": "string",
    "description": "string",
    "class": "string",
    "year": "integer",
    "country": "string",
    "publisher": "string",
    "circulation": "integer"
}
```