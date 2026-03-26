<div align="center">
  <h1>🧠 TaskIntelligence System</h1>
  <p><b>Smart Task Management with Automated Prioritization Engine</b></p>
  <p>
    <img src="https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk" alt="Java Version">
    <img src="https://img.shields.io/badge/Spring_Boot-4.0.3-green?style=for-the-badge&logo=springboot" alt="Spring Boot">
    <img src="https://img.shields.io/badge/Angular-21-red?style=for-the-badge&logo=angular" alt="Angular">
    <img src="https://img.shields.io/badge/Security-JWT-blue?style=for-the-badge&logo=jsonwebtokens" alt="Security">
    <img src="https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker" alt="Docker">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge" alt="License: MIT">
  </p>
</div>

---

## 🌍 Choose your language / Wybierz język
- [🇬🇧 English Version](#-english-version)
- [🇵🇱 Wersja Polska](#-wersja-polska)

---

## 🇬🇧 English Version

### 📖 Table of Contents
- [🚀 Overview](#-overview)
- [🏗️ Architecture & Core Modules](#️-architecture--core-modules)
- [📂 Project Structure](#-project-structure)
- [🧠 The Intelligence Algorithm](#-the-intelligence-algorithm)
- [🌐 API Endpoints & Capabilities](#-api-endpoints--capabilities)
- [🎨 Frontend State & Future Steps](#-frontend-state--future-steps)
- [🛠️ Installation & Setup](#️-installation--setup)
- [🗺️ Roadmap](#️-roadmap)
- [📧 Contact](#-contact)

### 🚀 Overview
**TaskIntelligence** is a modern, algorithm-driven productivity platform designed to eliminate decision fatigue. It goes beyond simple to-do lists by introducing a custom mathematical **Intelligence Engine** that automatically evaluates and ranks task priorities in real-time. Built entirely with a bleeding-edge tech stack (**Java 25**, **Spring Boot 4**, **Angular 21**), the project is engineered for performance, security, and scalability.

### 🏗️ Architecture & Core Modules
The backend follows Domain-Driven Design principles with clean separation of concerns:
- 🛡️ **Security (Auth):** Robust, strict `STATELESS` JWT authentication (`JJWT 0.12`) with `BCryptPasswordEncoder`, custom `JwtFilter`, and native Role-Based Access Control protecting endpoints.
- 🧠 **SmartTask Engine:** The algorithmic core that continuously calculates and categorizes the urgency of every task.
- ⏱️ **Task Scheduler:** Background services (`@Scheduled`) recalculating scores system-wide every hour, featuring database-level filtering of `COMPLETED` tasks to ensure horizontal scalability without N+1 overhead.
- 🧯 **Global Exception Handling:** Unified API error responses (`@ControllerAdvice`) across all controllers mapping exceptions to standardized HTTP codes.
- 💾 **Data Layer:** Clean `DTO` pattern matching with internal Entities, driving seamless interactions with `Hibernate` and `PostgreSQL`.
- ⚙️ **Infrastructure & Tooling:** Automated database migrations and mock data seeding via **Flyway**, boilerplate reduction and DTO mapping using **MapStruct** & **Lombok**, with environment-specific profiles (dev/prod).

### 📂 Project Structure
```text
TaskIntelligence/
├── backend/                  # Spring Boot 4.0.3 API
│   ├── src/main/java/...     # Core Java source code
│   ├── src/main/resources/   # Application configs & Flyway migrations
│   └── Dockerfile            # Backend container configuration
├── frontend/                 # Angular 21 Application (WIP)
│   ├── src/                  # Components, services, and styles
│   └── Dockerfile            # Frontend container configuration
└── compose.yaml              # Multi-container orchestration (DB, API, Web)
```

### 🧠 The Intelligence Algorithm
The system replaces guesswork with a precise mathematical model implemented in `TaskPriorityService`. The Urgency Score ($S$) is defined as:

$$S = \frac{Importance \times 10}{\max(1, DaysToDeadline)}$$

* **Dynamic Scaling:** As urgency builds, the score scales exponentially closer to the deadline.
* **Overdue Penalty System:** Tasks falling behind schedule automatically receive a critical **+50 point surge**, forcing them to the top of the user's focus.
* **Completion State:** Finishing a task reduces its score to **0**, archiving it and removing it from the active calculation pool.

### 🌐 API Endpoints & Capabilities
The entire API is cleanly documented using **Springdoc OpenAPI 3.1**, providing an interactive developer experience via Swagger UI (configured with global **BearerAuth** for seamless token testing):

**🔐 Authentication & Security**
* `POST /api/auth/register` - Secure user onboarding with password hashing.
* `POST /api/auth/login` - Secure login issuing access and refresh JWT tokens.
* `POST /api/auth/refresh` - Seamlessly refresh exhausted access tokens.
* `POST /api/auth/logout` - Securely invalidate sessions and refresh tokens.

**📋 Task Management (CRUD)**
* `GET / POST / PUT / DELETE /api/tasks` - Full Task lifecycle management.
* `GET /api/tasks/page` - Paginated task retrieval for improved performance.
* `PATCH /api/tasks/{id}/updateStatus` - Lightning-fast status updates.

**🧠 Smart Engine & Analytics**
* `GET /api/tasks/smart/getAllSmartTasks` - Main dashboard endpoint fetching tasks perfectly sorted by the $S$ score with pagination.
* `GET /api/tasks/smart/suggestions` - AI-driven suggestions for next steps.
* `POST /api/tasks/stats` - Endpoint to fetch comprehensive user productivity statistics.

**🛡️ Administration**
* `GET /api/admin/test` - Role-Based Access Control (RBAC) testing for Administrator roles.

### 🎨 Frontend State & Future Steps
> [!NOTE] 
> The Angular 21 application is currently in its scaffolding phase. The robust architecture is being mapped out, styled with `Tailwind CSS`, and will soon integrate seamlessly with the established RESTful API.

### 🛠️ Installation & Setup

To get this project up and running on your local machine, you have two options: using Docker (Recommended) or traditional local setup.

#### 🐳 Option 1: Docker Compose (Recommended)
The easiest way to start the entire environment (Database, Backend, and Frontend) without installing local dependencies.
1. Ensure you have **Docker** and **Docker Compose** installed.
2. Create a `.env` file in the root directory (next to `compose.yaml`) and define your database credentials:
   ```env
   DB_USERNAME=postgres
   DB_PASSWORD=secretpassword
   DB_NAME=taskintelligence_db
   ```
3. Run the following command in the root directory:
   ```bash
   docker compose up -d --build
   ```
   The API will be available at `http://localhost:8080` and the frontend at `http://localhost:4200`.

#### 💻 Option 2: Local Setup
**Prerequisites**
* **Java**: Version 25 (or compatible JDK)
* **Maven**: Latest stable version
* **Node.js & npm**: Latest stable version
* **PostgreSQL**: Database server running locally

**Backend Setup**
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/TaskIntelligence.git](https://github.com/your-username/TaskIntelligence.git)
    cd TaskIntelligence/backend
    ```
2.  **Configure Database:**
    * Create a PostgreSQL database (e.g., `taskintelligence_db`).
    * Update `src/main/resources/application.yml` (or `application-dev.yml`) with your specific credentials:
      ```yaml
      spring:
        datasource:
          url: jdbc:postgresql://localhost:5432/taskintelligence_db
          username: your_db_username
          password: your_db_password
      ```
3.  **Build and Run:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

**Frontend Setup (WIP)**
1.  **Navigate to the frontend directory:**
    ```bash
    cd ../frontend
    ```
2.  **Install dependencies & Run:**
    ```bash
    npm install
    ng serve
    ```

#### Running Tests
* **Backend:** `mvn test`
* **Frontend:** `ng test`

### 🗺️ Roadmap
* **Frontend Development:** Complete the Angular 21 application integration with the backend API.
* **Advanced Analytics:** Implement more sophisticated user productivity statistics and visualizations.
* **Notifications:** Add real-time notifications for upcoming deadlines or overdue tasks.
* **User Customization:** Allow users to customize priority calculation parameters.
* **Mobile Application:** Develop native mobile applications for iOS and Android.

### 📧 Contact
For any questions or collaborations, feel free to reach out:
* **Email**: [mateusz.rokitowski2004@gmail.com](mailto:mateusz.rokitowski2004@gmail.com)
* **GitHub**: [github.com/m4tiii](https://github.com/m4tiii)

---

## 🇵🇱 Wersja Polska

### 📖 Spis Treści
- [🚀 O Projekcie](#-o-projekcie)
- [🏗️ Architektura i Główne Moduły](#️-architektura-i-główne-moduły)
- [📂 Struktura Projektu](#-struktura-projektu)
- [🧠 Algorytm Intelligence](#-algorytm-intelligence)
- [🌐 Możliwości API](#-możliwości-api)
- [🎨 Stan Frontendu](#-stan-frontendu)
- [🛠️ Instalacja i Uruchomienie](#️-instalacja-i-uruchomienie)
- [🗺️ Plan Rozwoju](#️-plan-rozwoju)
- [📧 Kontakt](#-kontakt-1)

### 🚀 O Projekcie
**TaskIntelligence** to nowoczesna, algorytmiczna platforma produktywności, zaprojektowana by eliminować obciążenie decyzyjne z naszej codzienności. Projekt wykracza poza ramy zwykłych list "to-do", wprowadzając autorski **Silnik Intelligence**, który automatycznie bada i szereguje priorytety zadań w czasie rzeczywistym. Architektura wykorzystuje najnowsze standardy w branży (**Java 25**, **Spring Boot 4**, **Angular 21**), stawiając na wydajność, bezpieczeństwo i skalowalność gotową na rozwiązania typu Enterprise.

### 🏗️ Architektura i Główne Moduły
Backend został rygorystycznie zaprojektowany z myślą o czystym kodzie i podziale odpowiedzialności (Separation of Concerns):
- 🛡️ **Bezpieczeństwo (Auth):** Solidna autoryzacja (`JJWT 0.12`) wymuszająca politykę `STATELESS` powiązaną z `BCryptPasswordEncoder`, autorski `JwtFilter` oraz natywna kontrola dostępu typu RBAC.
- 🧠 **Silnik SmartTask:** Algorytmiczne serce systemu bezustannie oceniające pilność każdego wpisu.
- ⏱️ **Harmonogram (Scheduler):** Zautomatyzowane procesy tła (`@Scheduled`), zoptymalizowane do pomijania ukończonych zadań (`COMPLETED`) już na etapie zapytania do bazy, drastycznie zwiększając wydajność i redukując obciążenie przy dużej skali.
- 🧯 **Globalna Obsługa Błędów:** Scentralizowany kontroler przechwytujący i mapujący logikę wyjątków do jednorodnych odpowiedzi HTTP (`@ControllerAdvice`).
- 💾 **Warstwa Danych:** Bezpieczny przepływ danych ze wzorcem `DTO` we współpracy z silnikiem ORM w technologii `Hibernate` oraz `PostgreSQL`.
- ⚙️ **Infrastruktura i Narzędzia:** Zautomatyzowane migracje schematów bazy danych oraz wgrywanie danych autorskich (dummy data) przez **Flyway**, elastyczne mapowanie DTO z **MapStruct** i czysty kod dzięki bibliotece **Lombok**, wspierane odpowiednimi profilami wdrożeniowymi (dev/prod).

### 📂 Struktura Projektu
```text
TaskIntelligence/
├── backend/                  # API napisane w Spring Boot 4.0.3
│   ├── src/main/java/...     # Kod źródłowy (kontrolery, serwisy, encje)
│   ├── src/main/resources/   # Konfiguracja oraz migracje Flyway
│   └── Dockerfile            # Obraz kontenera dla backendu
├── frontend/                 # Aplikacja w Angular 21 (WIP)
│   ├── src/                  # Komponenty widoku
│   └── Dockerfile            # Obraz kontenera dla frontendu
└── compose.yaml              # Orkiestracja całości (Baza, API, Frontend)
```

### 🧠 Algorytm Intelligence
Logika `TaskPriorityService` eliminuje zgadywanie, kalkulując nieustannie Wskaźnik Pilności ($S$) ze wzoru:

$$S = \frac{Ważność \times 10}{\max(1, DniDoDeadline)}$$

* **Dynamiczne Skalowanie:** Znaczenie zadań gwałtownie rośnie w bezpośrednim pobliżu terminu wykonania (deadline).
* **System Kar (Overdue):** Zadania po upływie terminu zyskują automatycznie potężną premię **+50 punktów**, od razu windując się na sam szczyt uwagi użytkownika.
* **Archiwizacja:** Zamknięcie zadania (status "Completed") zeruje wynik ($0$), trwale zdejmując je z listy aktywnych wyzwań.

### 🌐 Możliwości API
Całość architektury jest dostępna i interaktywna dla programistów dzięki wdrożeniu systemu **Springdoc OpenAPI 3.1** (Swagger UI z globalnie wdrożoną autoryzacją **BearerAuth** pozwalającą na szybkie testy bez ciągłego odświeżania tokena pod każdym punktem końcowym):

**🔐 Autoryzacja i Bezpieczeństwo**
* `POST /api/auth/register` - Rejestracja z bezpiecznym haszowaniem haseł i przypisywaniem modeli.
* `POST /api/auth/login` - Klasyczne uwierzytelnianie wydające tokeny dostępowe oraz odświeżające (Refresh Token) w paradygmacie JWT.
* `POST /api/auth/refresh` - Bezszwowe odświeżanie tokenów dostępu.
* `POST /api/auth/logout` - Bezpieczne unieważnianie sesji i tokenów w bazie danych.

**📋 Zarządzanie Zadaniami (CRUD)**
* `GET / POST / PUT / DELETE /api/tasks` - Pełny cykl życia zadań ze wsparciem paginacji (`/api/tasks/page`).
* `PATCH /api/tasks/{id}/updateStatus` - Skondensowane, szybkie metody do zmiany aktualnych etapów zadań bez wysyłania pełnych paczek danych.

**🧠 Silnik Inteligentny i Analityka**
* `GET /api/tasks/smart/getAllSmartTasks` - Serce aplikacji zaciągające listę inteligentnie posortowanych zadań według $S$ (z obsługą stronicowania).
* `GET /api/tasks/smart/suggestions` - System inteligentnych sugestii zadań do wykonania.
* `POST /api/tasks/stats` - Generowanie i pobieranie zaawansowanych statystyk produktywności użytkownika.

**🛡️ Administracja**
* `GET /api/admin/test` - Testowe zabezpieczone endpointy wymagające ról administracyjnych (RBAC).

### 🎨 Stan Frontendu
> [!NOTE]
> Moduł kliencki oparty na technologii Angular 21 znajduje się we wczesnej fazie szkieletu (`boilerplate`). Fundamenty pod nowoczesny interfejs graficzny wspierany zaawansowanymi klasami `.css` z ekosystemu `Tailwind CSS` są właśnie mapowane na wyjścia backendowego API.

### 🛠️ Instalacja i Uruchomienie

Masz do wyboru dwie ścieżki uruchomienia: szybką przez środowisko Docker (Zalecane) lub manualną konfigurację lokalną.

#### 🐳 Opcja 1: Docker Compose (Zalecane)
Najszybsza opcja niewymagająca instalowania Javy czy bazy danych na własnym systemie.
1. Upewnij się, że masz zainstalowanego **Dockera** oraz **Docker Compose**.
2. Utwórz plik `.env` w głównym katalogu projektu (obok pliku `compose.yaml`) i wprowadź swoje dane do bazy:
   ```env
   DB_USERNAME=postgres
   DB_PASSWORD=secretpassword
   DB_NAME=taskintelligence_db
   ```
3. W głównym folderze projektu odpal komendę:
   ```bash
   docker compose up -d --build
   ```
   PostgreSQL, Spring Boot (port 8080) i Angular (port 4200) wstaną automatycznie.

#### 💻 Opcja 2: Instalacja Lokalna
**Wymagania wstępne**
* **Java**: Wersja 25 (lub kompatybilny JDK)
* **Maven**: Najnowsza stabilna wersja
* **Node.js & npm**: Najnowsza stabilna wersja
* **PostgreSQL**: Serwer bazy danych uruchomiony lokalnie

**Konfiguracja Backendu**
1.  **Sklonuj repozytorium:**
    ```bash
    git clone [https://github.com/your-username/TaskIntelligence.git](https://github.com/your-username/TaskIntelligence.git)
    cd TaskIntelligence/backend
    ```
2.  **Skonfiguruj Bazę Danych:**
    * Utwórz bazę danych PostgreSQL (np. `taskintelligence_db`).
    * Zaktualizuj plik `src/main/resources/application.yml` (lub `application-dev.yml`) swoimi danymi:
      ```yaml
      spring:
        datasource:
          url: jdbc:postgresql://localhost:5432/taskintelligence_db
          username: your_db_username
          password: your_db_password
      ```
3.  **Zbuduj i Uruchom:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

**Konfiguracja Frontendu (WIP)**
1.  **Przejdź do katalogu frontendu:**
    ```bash
    cd ../frontend
    ```
2.  **Zainstaluj zależności i uruchom:**
    ```bash
    npm install
    ng serve
    ```

#### Uruchamianie Testów
* **Testy Backendu:** `mvn test`
* **Testy Frontendu:** `ng test`

### 🗺️ Plan Rozwoju
* **Rozwój Frontendu:** Ukończenie integracji aplikacji Angular 21 z API backendu.
* **Zaawansowana Analityka:** Wdrożenie bardziej zaawansowanych statystyk produktywności użytkownika i wizualizacji.
* **Powiadomienia:** Dodanie powiadomień w czasie rzeczywistym o nadchodzących terminach lub zaległych zadaniach.
* **Personalizacja Użytkownika:** Umożliwienie użytkownikom dostosowywania parametrów obliczania priorytetów.
* **Aplikacja Mobilna:** Opracowanie natywnych aplikacji mobilnych dla systemów iOS i Android.

### 📧 Kontakt
W przypadku pytań lub chęci współpracy, skontaktuj się:
* **Email**: [mateusz.rokitowski2004@gmail.com](mailto:mateusz.rokitowski2004@gmail.com)
* **GitHub**: [github.com/m4tiii](https://github.com/m4tiii)

---

## 🛠 Tech Stack / Technologie

| Component | Technology |
| :--- | :--- |
| **Backend Core** | Java 25, Spring Boot 4.0.3 |
| **Data Layer** | PostgreSQL, Hibernate ORM, Flyway Migrations, MapStruct |
| **Frontend Core** | Angular 21.2.2 (WIP), Tailwind CSS |
| **Security** | Spring Security + JJWT 0.12 (Stateless JWT) |
| **API Docs** | Springdoc OpenAPI 3.1 (Swagger UI) |
| **Deployment** | Docker, Docker Compose |

---

<br/>
<div align="center">
  <b>Developed with ❤️ by m4tiii</b>
</div>
