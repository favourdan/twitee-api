# Twitee App
>Twitee App is a microservice application and a simplified platform to express their thoughts.
This project utilizes Spring Boot as the backend framework.
Below is a detailed technical design to guide the development process.

## Authentication
>For the app, ach user is required to register and log in with account details comprising their name, email, password, and the date of creation.
Upon registration, the application sends an onboarding email to the user for a seamless introduction to TWITEE.

## Features

### User Authentication:
> ● Users can register and log in using a secure authentication method,preferably JSON Web Token (JWT).`

> ● Account creation and login involve the use of email and password

### Twit Operations:

 ~~~
● Authenticated users can post twits (tweets) to share their thoughts.
● Users can delete their own twits, ensuring ownership verification. 

For Interactions:
● Authenticated users can comment on any twit.
● Users can like twits, limited to one like per user per twit. 
~~~

## Technical Details
> For this application, we have four services running:
> To access the Swagger page, ensure that the api-gateway application is running
> ___http://localhost:8060/webjars/swagger-ui/index.html___

### 1. api-gateway
The api gateway runs on port `8060` with url `http://localhost:8060`
The gateway is the entry point for all the services. Only its port is
exposed and from there we connect to other services such as twit,
authentication and comment

### 2. Service Discovery
The Service discovery is the Eureka web server which registers all the
instances of our services with the api-gateway included.
The Eureka web can be accessed using the url
`http://localhost:8060/eureka/web` `8060`
being the api-gateway url. To access the eureka web server,
make sure api-gateway is running.

When the eureka page loads the authentication is
___username:___ `eureka` and ___password___: `password`

### 3. Authentication Service
This service is responsible for authenticating users for login and registration,
verification of registration via email as well as authorization.
___URL:___ `http://localhost:8060/api/v1/auth`

##### Register:
~~~ 
endpoint: /register
method: POST
        request: 
        {
            "name": "string",
            "email": "string",
            "password": "eIPA\\ ?Z<0D)Vq"
        }
~~~

##### Login:
~~~ 
endpoint: /login
method: POST
    request: 
        {
            "email": "string",
            "password": "eIPA\\ ?Z<0D)Vq"
        }
~~~

##### Verify Registration:
Pass the token received in the mail or click link from mail.
~~~ 
endpoint: /verify-registration?token=8kgkgk398394847kdgkju;pwifle
method: GET
~~~

### 3. Comments Service
This handles comments made by an authenticated user.
Comments are made on already posted twits.
___URL:___ `http://localhost:8060/api/v1/comments`

##### Post A Comment:
~~~ 
endpoint: /register
method: POST
    request: 
        {
          "appUserId": 0,
          "twitId": 0,
          "comment": "string"
        }
~~~

### 3. Twit Service
This service is responsible for

___URL:___ `http://localhost:8060/api/v1/comments`

##### Post A Twit:
~~~ 
endpoint: /twits
method: POST
    request: 
        {
          "body": "string",
          "title": "string"
        }
~~~

##### Delete Twit By ID:
~~~
endpoint: /twits/{twitId}
method: DELETE
    request: 
        {
          "body": "string",
          "title": "string"
        }
~~~

#### Like or Unlike Comment By ID
~~~
endpoint: /twits/{twitId}/like
method: POST
~~~

#### Delete Twit By ID
~~~ 
Fetches all twits
endpoint: /twits
method: GET
~~~

