# Drive Pet

Drive Pet is an online web file storage application where users can create folders, save files, and share access with other users.  
It was developed as part of the **semester project** for the **BI-EJK** course.

The project uses a modern tech stack: **Java (Quarkus)**, **React**, **PostgreSQL**, and file storage powered by **LocalStack**.

---

## 🚀 Features

- Create, edit, and delete folders
- Upload and manage files
- Preview images and videos
- Share file access with specific users
- Access management with fine-grained permissions
- RESTful Backend and frontend integration, JWT security realization

---

## 🏗 Project Structure

/api → Backend (Quarkus, Java) 

/frontend → Frontend (React) 

---

## ⚙️ Tech Stack

- **Backend:** Java, Quarkus
- **Frontend:** React
- **Database:** PostgreSQL
- **File Storage:** LocalStack (emulating AWS S3)
- **Containerization:** Docker, Docker Compose
- **API Documentation**: Swagger (OpenAPI)

---

## 🛠 Installation

### Prerequisites

- Docker & Docker Compose installed
- Java 21+
- Node.js 20+

### Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/drive-pet.git
cd drive-pet
```

### Step 2: Start Docker Services

```bash
docker-compose up -d
```

### Step 3: Start the Backend

```bash
cd api
./mvnw quarkus:dev
```

### Step 4: Start the Frontend

```bash
cd ../frontend
npm install
npm start
```

### 🧪 Running Tests

Backend Tests

```bash
cd api
./mvnw test
```

Frontend Tests

```bash
cd frontend
npm test
```

### 📦 Build for Production

Backend

```bash
cd api
./mvnw package
```

Frontend

```bash
cd frontend
npm run build
```

### 📖 API Documentation (Swagger)
The backend API is documented using Swagger (OpenAPI).

After running the backend, you can access:

OpenAPI spec (JSON/YAML):
http://localhost:8080/openapi

Swagger UI (interactive docs):
http://localhost:8080/swagger-ui

Here you can explore, test, and interact with the API endpoints directly from the browser.
