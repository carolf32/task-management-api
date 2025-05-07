# Tasks Management API Documentation
##  Purpose
This API was developed to manage tasks associated with users.
Users can register, log in, and then create, view, update, and delete their own tasks.

Authentication is based on JWT tokens.
All routes, except login, require authentication.

## How to Run the Project
Clone the repository

```bash
git clone https://github.com/carolf32/task-management-api
```

Enter the project folder
```bash
cd tasks-management-api
```

Configure your database in <strong>application.properties</strong> </br>
Run the project
```bash
./mvnw spring-boot:run
```

##  Authentication
To access protected routes, you must send the JWT token in the request header:

```http
Authorization: Bearer {your-token}
```
#  Technologies Used
Java 17</br>
Spring Boot</br>
Spring Security</br>
Spring Data JPA</br>
JWT (JSON Web Token)</br>
Lombok</br>
Database (PostgreSQL)</br>

#  API Endpoints

##  Users

|Method	|Route|	Protected?	|Description|
|-|-|-|-|
|POST	|/users|	❌|	Create a new user|
|GET	|/users|	✅|	List all users|
|GET	|/users/{id}|	✅|	Get a user by ID|
|GET	|/users/email/{email}|	✅|	Get a user by email|
|PUT	|/users/{id}	|✅	|Update a user|
|DELETE|	/users/{id}	|✅|	Delete a user|

##  Tasks

|Method|	Route|	Protected?|	Description|
|-|-|-|-|
|POST	|/tasks|	✅|	Create a new task (associated with the authenticated user)|
|GET|	/tasks|	✅|	List all tasks of the authenticated user|
|GET|	/tasks/{id}|	✅|	Get a specific task by ID|
|GET|	/tasks/status/{completed}|	✅|	Get tasks by completion status (completed/incomplete)|
|GET|	/tasks/status/{days}|	✅|	Get tasks by days until deadline|
|GET|	/tasks/search	|✅|	Search tasks by title|
|PUT|	/tasks/{id}|	✅|	Update a task|
|DELETE	|/tasks/{id}|	✅|	Delete a task|


##  Authentication
### Login

|Method	|Route	|Protected?|	Description|
|-|-|-|-|
|POST	|/auth/login|	❌	|Login and receive a JWT token|

Request (Parameters):
```http
email=your@email.com
password=yourPassword
```
Response:
```json
"your-jwt-token-here"
```

##  Users
### Create User

|Method	|Route	|Protected?	|Description|
|-|-|-|-|
|POST	|/users|	❌	|Create a new user|

Request Body:

```json
{
  "username": "yourUsername",
  "email": "your@email.com",
  "password": "yourPassword"
}
```
Response:
```json
{
  "id": 1,
  "username": "yourUsername",
  "email": "your@email.com",
  "password": "$2a$10$encryptedpasswordhash"
}
```

### Get All Users

|Method|	Route	|Protected?	|Description|
|-|-|-|-|
|GET	|/users|	✅|	List all users|

Response:
```json
[
  {
    "id": 1,
    "username": "yourUsername",
    "email": "your@email.com",
    "password": "$2a$10$encryptedpasswordhash"
  }
]
```

### Get User by ID

|Method	|Route|	Protected?	|Description|
|-|-|-|-|
|GET|	/users/{id}|	✅|	Get a user by ID|

Response:
```json
{
  "id": 1,
  "username": "yourUsername",
  "email": "your@email.com",
  "password": "$2a$10$encryptedpasswordhash"
}
```

### Get User by Email

|Method|	Route	|Protected?|	Description|
|-|-|-|-|
|GET|	/users/email/{email}|	✅	|Get a user by email|

Response:
(Same as Get User by ID)

### Update User

|Method	|Route|	Protected?	|Description|
|-|-|-|-|
|PUT	|/users/{id}|	✅	|Update a user|

Request Body:
```json
{
  "username": "updatedUsername",
  "email": "updated@email.com",
  "password": "newPassword"
}
```
Response:
(Same as Create User, but with updated information.)

### Delete User

|Method	|Route	|Protected?	|Description|
|-|-|-|-|
|DELETE|	/users/{id}|	✅|	Delete a user|

Response:
(No content. Status 200 or 204.)

##  Tasks
### Create Task

|Method	|Route	|Protected?|	Description|
|-|-|-|-|
|POST|	/tasks|	✅	|Create a new task associated with authenticated user|

Request Body:
```json
{
  "task": "Task Title",
  "daysUntilDeadline": 2,
  "completed": false
}
```
Response:
```json
{
  "id": 1,
  "task": "Task Title",
  "daysUntilDeadline": 2,
  "completed": false,
  "createdAt": "2025-04-26T15:30:00",
  "user": {
    "id": 1,
    "username": "yourUsername",
    "email": "your@email.com",
    "password": "$2a$10$encryptedpasswordhash"
  }
}
```

### Get All Tasks

|Method	|Route	|Protected?	|Description|
|-|-|-|-|
|GET	|/tasks|	✅|	List all tasks of the authenticated user|

Response:
```json
[
  {
    "id": 1,
    "task": "Task Title",
    "daysUntilDeadline": 2,
    "completed": false,
    "createdAt": "2025-04-26T15:30:00",
    "user": { ... }
  }
]
```

### Get Task by ID

|Method	|Route	|Protected?|	Description|
|-|-|-|-|
|GET|	/tasks/{id}|	✅|	Get a task by its ID|

Response:
(Same as Get All Tasks, but a single task.)

### Get Tasks by Status

|Method	|Route|	Protected?|	Description|
|-|-|-|-|
|GET|	/tasks/status/{completed}|	✅|	Get tasks by completed status (true or false)|

Response:
(Same as Get All Tasks, filtered by status.)

### Get Tasks by Days Until Deadline

|Method	|Route|	Protected?|	Description|
|-|-|-|-|
|GET|	/tasks/status/{days}|	✅|	Get tasks by days until deadline|

Response:
(Same as Get All Tasks, filtered by days until deadline.)

### Search Tasks by Title

|Method|	Route|	Protected?	|Description|
|-|-|-|-|
|GET	|/tasks/search|	✅	|Search tasks containing a title substring|

Response:
(Same as Get All Tasks, filtered by title.)

### Update Task

|Method	|Route|	Protected?|	Description|
|-|-|-|-|
|PUT	|/tasks/{id}|	✅	|Update a task|

Request Body:
```json
{
  "task": "Updated Task Title",
  "daysUntilDeadline": 5
}
Response:
(Same as Create Task, but with updated information.)
```

### Delete Task

|Method|	Route|	Protected?|	Description|
|-|-|-|-|
|DELETE	|/tasks/{id}|	✅	|Delete a task|

Response:
(No content. Status 200 or 204.)
