# Jigsaw Sudoku

Jigsaw Sudoku is a twist on the classic Sudoku game where regions have irregular shapes!
The player must fill the grid so that every row, column, and unique region contains all numbers from 3 to 9 without
repetition.

---

## Technologies

- **Frontend:** Angular
- **Backend:** Java Spring Boot WebServer
- **Database:** PostgreSQL
- **Styles:** SCSS
- **API Communication:** REST

---

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/bromscandium/jigsawsudoku.git
cd jigsaw-sudoku
```

### 2. Launch the Frontend (Angular)

```bash
cd frontend
npm install
npm run start
```

Frontend will be available at [http://localhost:4200](http://localhost:4200).

> You should have an environment file (environment.ts) in the ./frontend/src/environment/ with URL to your backend.

### 3. Launch the Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

Backend will run at [http://localhost:8081](http://localhost:8081).

> Make sure PostgreSQL is running, and database credentials are correctly set in `application.properties`. Also in root
> folder should be .env file with those variables: DATABASE_URL, USERNAME, PASSWORD (for database setup),
> ALLOWED_ORIGIN (
> for frontend access), COMMENT_SERVICE_URL, RATING_SERVICE_URL, SCORE_SERVICE_URL, AUTH_SERVICE_URL (links for Backend
> API).

---

## Features

- Unique Jigsaw Sudoku board generation
- Solution validation
- Hint system
- Game progress saving and loading
- Player personal scores saved in the database
- Leaderboard with top players
- User-friendly design
- Support for two languages
- Light and dark themes
- Session with cookie

---

## 📸 Screenshots

![ (2).png](screenshots/%20%282%29.png)
![ (3).png](screenshots/%20%283%29.png)
![ (4).png](screenshots/%20%284%29.png)
![ (5).png](screenshots/%20%285%29.png)
![ (6).png](screenshots/%20%286%29.png)
![ (7).png](screenshots/%20%287%29.png)
![ (8).png](screenshots/%20%288%29.png)
![ (9).png](screenshots/%20%289%29.png)
![ (10).png](screenshots/%20%2810%29.png)
![ (11).png](screenshots/%20%2811%29.png)
![ (1).png](screenshots/%20%281%29.png)
![(12).png](screenshots/%2812%29.png)

---

> Live, take risks, and never give up!

