
# API Endpoints Documentation
## Authentication Routes
### `POST /auth/auth-user`
Verifies the user's ID token and, if necessary, creates the user in the database.
- **Request Body**:
  ```json
  {
    "idToken": "string",
    "userId": "string",
    "deviceToken": "string"
  }
  ```
- Responses:  
  - 200 OK:
  ```json
  {
    "message": "Token verified successfully"
  } 
  ```
  - 403 Forbidden:
  ```json
  {
    "error": "No token provided"
  }
  ```
  - 500 Internal Server Error:
  ```json
  {
    "error": "Error verifying token"
  }
  ```
## Mission Routes
### `POST /missions/complete-mission`
Marks a mission as completed for a user.
#### Request Body:
```json
{
  "userId": "string",
  "idMission": "number"
}
```
#### Responses:
  - 200 OK:
```json
{
"message": "Mission marked as completed"
}
```  
  - 403 Forbidden: 
```json
{
  "error": "Missing data"
}
```
  - 404 Not Found:
```json
{
  "error": "Mission not found or already completed"
}
```
- 500 Internal Server Error:
```json
{
  "error": "Error completing mission"
}
```
### `GET /missions/get-daily-mission`
Fetches the user’s assigned mission for the current day.
#### Query Parameters:
- userId: The ID of the user (required).
#### Responses:
- 200 OK:
```json
{
    "mission": {
        "id_mission": "number",
        "titulo": "string",
        "descripcion": "string", 
        "categoria": "string",
        "dificultad": "string"
    },
    "isMissionCompleted": "boolean"
}
```
- 404 Not Found:
```json
{
  "error": "No mission found"
}
```
- 500 Internal Server Error:
```json
{
  "error": "Error fetching daily mission"
}
```
### `GET /missions/fetch-all-missions`
Fetches all missions from the database.
#### Responses:
- 200 OK:
```json
[
  {
    "id_mission": "number",
    "titulo": "string",
    "descripcion": "string",
    "categoria": "string",
    "dificultad": "string"
  },
"..."
]
```
- 500 Internal Server Error:
```json
{
  "error": "Error fetching missions"
}
```
### `GET /missions/get-optional-missions`
Fetches optional missions from the database.
#### Query Parameters:
- userId: The ID of the user (required).
#### Example responses:
- 200 OK:
```json
[
  {
    "id_mision": 39,
    "titulo": "Misión 29",
    "descripcion": "Descripción de misión 29",
    "categoria": "Categoría B",
    "dificultad": "facil"
  },
  {
    "id_mision": 14,
    "titulo": "Misión 4",
    "descripcion": "Descripción de misión 4",
    "categoria": "Categoría B",
    "dificultad": "facil"
  },
  {
    "id_mision": 58,
    "titulo": "Misión 48",
    "descripcion": "Descripción de misión 48",
    "categoria": "Categoría C",
    "dificultad": "facil"
  }
]
```
- 404 Not Found:
```json
{
  "error": "No optional missions found"
}
```
```json
{
  "error": "Missing query data"
}
```
- 500 Internal Server Error:
```json
{
  "error": "Error fetching optional missions"
}
```
### `POST /missions/reroll-opt-mission`
Rerolls an optional mission for a user.
#### Request Body:
```json
{
    "userId": "106199581597724590099",
    "idMission": 44
}
```
#### Example response: 
- 200 OK
```json
{
    "mission": {
        "id_mision": 46,
        "titulo": "Misión 36",
        "descripcion": "Descripción de misión 36",
        "categoria": "Categoría C",
        "dificultad": "facil"
    }
}
```
### `POST /missions/complete-opt-mission`
Completes an optional mission for a user.
#### Request Body:
```json
{
    "userId": "106199581597724590099",
    "idMission": 44
}
```
#### Example response: 
- 200 OK
```json
{
    "mission": {
        "id_mision": 46,
        "titulo": "Misión 36",
        "descripcion": "Descripción de misión 36",
        "categoria": "Categoría C",
        "dificultad": "facil"
    }
}
```

## Stats Routes
### `GET /stats/get-leaderboard`
This endpoint retrieves the leaderboard information, listing users and their respective total points accumulated.
#### Example Response
- 200 OK
```json
{
    "leaderboard": [
        {
            "id_usuario": "user1",
            "nombre": "Usuario 1",
            "total_puntos": "115"
        },
        {
            "id_usuario": "user2",
            "nombre": "Usuario 2",
            "total_puntos": "80"
        },
        {
            "id_usuario": "user3",
            "nombre": "Usuario 3",
            "total_puntos": "45"
        },
        {
            "id_usuario": "lp3qrCX98gWVcJfdoBc8vPRnxCD3",
            "nombre": "development_tests",
            "total_puntos": "40"
        }
    ]
}
```

### `GET /stats/get-user-stats`
This endpoint returns the total number of missions completed by a user, as well as their current and longest mission streak.
#### Query Parameters
- `userId` (required): The ID of the user for whom to retrieve the stats.
#### Example Request
GET `/get-user-stats?userId=201`
#### Example Response
- 200 OK
```json
{
    "userId": "user201",
    "rachaActual": 0,
    "rachaMaxima": 2,
    "totalPuntos": "52",
    "totalMisionesCompletadas": 6
}
```

### `GET /stats/get-xp-bar-data`
This endpoint returns the current XP bar data for a user.
#### Query Parameters
- `userId` (required): The ID of the user for whom to retrieve the XP bar data.
#### Example Request
GET `/stats/get-xp-bar-data?userId=201`
#### Example Response
- 200 OK
```json
{
    "miPosicion": 2,
    "misPuntos": 52,
    "puntosSiguienteUsuario": 45
}
```

### `GET /stats/get-points-day`
This endpoint returns the total points earned by a user on each day within the last 7 days.

#### Query Parameters
- `userId` (required): The ID of the user whose points data is being requested.

#### Example Request
GET `/stats/points-day?userId=12345`

#### Example Response
- 200 OK
    ```json
    [
        {
            "dia": "15-10-2024",
            "puntos_totales": 12
        },
        {
            "dia": "16-10-2024",
            "puntos_totales": 20
        },
        {
            "dia": "17-10-2024",
            "puntos_totales": 15
        }
    ]
    ```

### `GET /stats/get-user-achievements`
This endpoint all achievements and whether they have been completed or not.

#### Query Parameters
- `userId` (required): The ID of the user for whom to retrieve the achievements.

#### Example Request
GET `/stats/get-user-achievements?userId=12345`

#### Example Response
- 200 OK
  ```json
  {
    "achievements": [
        {
            "id_achievement": 1,
            "isCompleted": 1
        },
        {
            "id_achievement": 2,
            "isCompleted": 0
        },
        {
            "id_achievement": 3,
            "isCompleted": 1
        },
        {
            "id_achievement": 4,
            "isCompleted": 0
        }
    ]
  }
  ```
  
### `GET /stats/get-user-trophies`
This endpoint returns the trophies earned by a user.

#### Query Parameters
- `userId` (required): The ID of the user for whom to retrieve the trophies.

#### Example Request
GET `/stats/get-user-trophies?userId=12345`

#### Example Response
- 200 OK
```json
{
    "trophyCount": {
        "total_oro": 0,
        "total_plata": 1,
        "total_bronce": 0
    }
}
```