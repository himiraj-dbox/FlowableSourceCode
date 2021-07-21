# Flowable All in One image

This image contains all Flowable UI apps running on Tomcat.

```
docker run -p 8080:8080 flowable/all-in-one
```

### Include Flowable apps
* Flowable IDM  (http://localhost:8080/DB-idm)
* Flowable Modeler  (http://localhost:8080/DB-modeler)
* Flowable Task  (http://localhost:8080/DB-task)
* Flowable Admin  (http://localhost:8080/DB-admin)

### Tomcat

version 8.5.34

### Database

in memory h2

### Default user

user: admin  
password: test