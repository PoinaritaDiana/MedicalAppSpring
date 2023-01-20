# Medical Application using Spring Boot

### General description and functionalities of the application

----  

### Entities and services

##### Database diagram:

----

### Business requirements

----

### Packages
All classes are grouped in several packages:
- model: contains the classes for the 7 entities
- service: beans for defining services (implementing business logic), one per feature
- repository: beans for defining repositories, one for each entity
- controller: beans responsible for processing incoming REST API for each feature
![img_1.png](img/img_1.png)

----

### Validation
- There are validations at the model level for the fields of an entity:
![img_4.png](img/img_4.png)   
    

- To catch any exception as early as possible, the validation for the entity is done in the controller, as well as for any other parameters:
![img_5.png](img/img_5.png)


- Also, custom exceptions are defined for various situations, which are handled in a ControllerAdvice:
![img_3.png](img/img_3.png)

----

### Unit tests
![img_2.png](img/img_2.png)

#### For Service classes: 
![img_3.png](img/img_service_tests.png)

#### For Controller classes
![img_1.png](img/img_controller_tests.png)
----

### Swagger documentation

After running the app, you can test it in browser by visiting `http://localhost:8080/swagger-ui/index.html#/`.

----

### Functionality demonstrated using Postman

----

