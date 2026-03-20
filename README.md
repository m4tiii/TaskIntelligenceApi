<div align="center">
  <h1>🧠 TaskIntelligence System</h1>
  <p><b>Smart Task Management with Automated Prioritization Engine</b></p>
  <p>
    <img src="https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk" alt="Java Version">
    <img src="https://img.shields.io/badge/Spring_Boot-4.0.3-green?style=for-the-badge&logo=springboot" alt="Spring Boot">
    <img src="https://img.shields.io/badge/Angular-21-red?style=for-the-badge&logo=angular" alt="Angular">
    <img src="https://img.shields.io/badge/Security-JWT-blue?style=for-the-badge&logo=jsonwebtokens" alt="Security">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge" alt="License: MIT">
  </p>
</div>

---

## 🌍 Choose your language / Wybierz język
- [🇬🇧 English Version](#-english-version)
- [🇵🇱 Wersja Polska](#-wersja-polska)

---

## 🇬🇧 English Version

### 🚀 Overview
**TaskIntelligence** is a modern, algorithm-driven productivity platform designed to eliminate decision fatigue. It goes beyond simple to-do lists by introducing a custom mathematical **Intelligence Engine** that automatically evaluates and ranks task priorities in real-time. Built entirely with a bleeding-edge tech stack (**Java 25**, **Spring Boot 4**, **Angular 21**), the project is engineered for performance, security, and scalability.

### 🏗️ Architecture & Core Modules
The backend follows Domain-Driven Design principles with clean separation of concerns:
- 🛡️ **Security (Auth):** Robust, stateless JWT authentication (`JJWT 0.12`), custom `JwtFilter`, and Role-Based Access Control.
- 🧠 **SmartTask Engine:** The algorithmic core that continuously calculates and categorizes the urgency of every task.
- ⏱️ **Task Scheduler:** Background services (`@Scheduled`) recalculating scores system-wide every hour to ensure data reflects reality.
- 🧯 **Global Exception Handling:** Unified API error responses (`@ControllerAdvice`) across all controllers mapping exceptions to standardized HTTP codes.
- 💾 **Data Layer:** Clean `DTO` pattern matching with internal Entities, driving seamless interactions with `Hibernate` and `PostgreSQL`.

### 🧠 The Intelligence Algorithm
The system replaces guesswork with a precise mathematical model implemented in `TaskPriorityService`. The Urgency Score ($S$) is defined as:

$$S = \frac{Importance \times 10}{\max(1, DaysToDeadline)}$$

* **Dynamic Scaling:** As urgency builds, the score scales exponentially closer to the deadline.
* **Overdue Penalty System:** Tasks falling behind schedule automatically receive a critical **+50 point surge**, forcing them to the top of the user's focus.
* **Completion State:** Finishing a task reduces its score to **0**, archiving it and removing it from the active calculation pool.

### 🌐 API Endpoints & Capabilities
The entire API is cleanly documented using **Springdoc OpenAPI 3.1**, providing an interactive developer experience via Swagger UI:
* `POST /api/auth/register` - Secure user onboarding with password hashing.
* `POST /api/auth/login` - Secure login issuing JWT tokens.
* `GET /api/tasks/smart` - Main dashboard endpoint fetching tasks perfectly sorted by the $S$ score.
* `PATCH /api/tasks/{id}/status` - Lightning-fast status updates.

### 🎨 Frontend State & Future Steps
> [!NOTE] 
> The Angular 21 application is currently in its scaffolding phase. The robust architecture is being mapped out, styled with `Tailwind CSS`, and will soon integrate seamlessly with the established RESTful API.

---

## 🇵🇱 Wersja Polska

### 🚀 O Projekcie
**TaskIntelligence** to nowoczesna, algorytmiczna platforma produktywności, zaprojektowana by eliminować obciążenie decyzyjne z naszej codzienności. Projekt wykracza poza ramy zwykłych list "to-do", wprowadzając autorski **Silnik Intelligence**, który automatycznie bada i szereguje priorytety zadań w czasie rzeczywistym. Architektura wykorzystuje najnowsze standardy w branży (**Java 25**, **Spring Boot 4**, **Angular 21**), stawiając na wydajność, bezpieczeństwo i skalowalność gotową na rozwiązania typu Enterprise.

### 🏗️ Architektura i Główne Moduły
Backend został rygorystycznie zaprojektowany z myślą o czystym kodzie i podziale odpowiedzialności (Separation of Concerns):
- 🛡️ **Bezpieczeństwo (Auth):** Solidna, bezstanowa autoryzacja oparta na tokenach JWT (`JJWT 0.12`), autorski `JwtFilter` oraz polityka przypisywania ról.
- 🧠 **Silnik SmartTask:** Algorytmiczne serce systemu bezustannie oceniające pilność każdego wpisu.
- ⏱️ **Harmonogram (Scheduler):** Zautomatyzowane procesy tła (`@Scheduled`), które co godzinę masowo odświeżają punktację w bazie danych.
- 🧯 **Globalna Obsługa Błędów:** Scentralizowany kontroler przechwytujący i mapujący logikę wyjątków do jednorodnych odpowiedzi HTTP (`@ControllerAdvice`).
- 💾 **Warstwa Danych:** Bezpieczny przepływ danych ze wzorcem `DTO` we współpracy z silnikiem ORM w technologii `Hibernate` oraz `PostgreSQL`.

### 🧠 Algorytm Intelligence
Logika `TaskPriorityService` eliminuje zgadywanie, kalkulując nieustannie Wskaźnik Pilności ($S$) ze wzoru:

$$S = \frac{Ważność \times 10}{\max(1, DniDoDeadline)}$$

* **Dynamiczne Skalowanie:** Znaczenie zadań gwałtownie rośnie w bezpośrednim pobliżu terminu wykonania (deadline).
* **System Kar (Overdue):** Zadania po upływie terminu zyskują automatycznie potężną premię **+50 punktów**, od razu windując się na sam szczyt uwagi użytkownika.
* **Archiwizacja:** Zamknięcie zadania (status "Completed") zeruje wynik ($0$), trwale zdejmując je z listy aktywnych wyzwań.

### 🌐 Możliwości API
Całość architektury jest dostępna i interaktywna dla programistów dzięki wdrożeniu systemu **Springdoc OpenAPI 3.1** (Swagger UI):
* `POST /api/auth/register` - Rejestracja z bezpiecznym haszowaniem haseł i przypisywaniem modeli.
* `POST /api/auth/login` - Klasyczne uwierzytelnianie na podstawie danych do logowania wydające tokeny autoryzacyjne w paradygmacie JWT.
* `GET /api/tasks/smart` - Serce aplikacji zaciągające listę inteligentnie posortowanych zadań według $S$.
* `PATCH /api/tasks/{id}/status` - Skondensowane, szybkie metody do zmiany aktualnych etapów zadań bez wysyłania pełnych paczek danych.

### 🎨 Stan Frontendu
> [!NOTE]
> Moduł kliencki oparty na technologii Angular 21 znajduje się we wczesnej fazie szkieletu (`boilerplate`). Fundamenty pod nowoczesny interfejs graficzny wspierany zaawansowanymi klasami `.css` z ekosystemu `Tailwind CSS` są właśnie mapowane na wyjścia backendowego API.

---

## 🛠 Tech Stack / Technologie

| Component | Technology |
| :--- | :--- |
| **Backend Core** | Java 25, Spring Boot 4.0.3 |
| **Data Layer** | PostgreSQL, Hibernate ORM, DTO Mappers |
| **Frontend Core** | Angular 21.2.2 (WIP), Tailwind CSS |
| **Security** | Spring Security + JJWT 0.12 (Stateless JWT) |
| **API Docs** | Springdoc OpenAPI 3.1 (Swagger UI) |

---

<br/>
<div align="center">
  <b>Developed with ❤️ by m4tiii</b>
</div>
