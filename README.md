# 🧠 TaskIntelligence System
> **Smart Task Management with Automated Prioritization | Spring Boot 4 & Angular 21**

[![Java Version](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-green?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-21-red?style=for-the-badge&logo=angular)](https://angular.dev/)
[![Security](https://img.shields.io/badge/Security-JWT-blue?style=for-the-badge&logo=jsonwebtokens)](https://jwt.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

---

## 🌍 Choose your language / Wybierz język
- [English Version](#-english-version)
- [Wersja Polska](#-wersja-polska)

---

## 🇬🇧 English Version

### 🚀 Overview
**TaskIntelligence** is a modern full-stack application designed to optimize daily productivity. Unlike standard to-do lists, it features an automated **Intelligence Engine** that ranks tasks by urgency and importance in real-time. The project utilizes a "bleeding-edge" stack with **Spring Boot 4** and **Java 25**.

### ✨ Key Features
* 🤖 **Smart Priority Engine** – Automatic calculation of task scores based on deadlines.
* 🔐 **JWT Security** – Stateless authentication for registration and login flows.
* 📅 **Task Scheduler** – Background service that refreshes all task priorities every hour.
* 📄 **RESTful API** – Clean endpoints with full pagination support and Swagger documentation.
* 🎨 **Angular 21 Frontend** – Modern web interface (currently in initial boilerplate phase).

### 🧪 Mathematical Priority Logic
The system implements a specific algorithm in `TaskPriorityService` to determine task urgency ($S$):

$$S = \frac{Importance \times 10}{\max(1, DaysToDeadline)}$$

**Logic breakdown:**
- **Standard:** The score increases as the deadline approaches.
- **Overdue:** Tasks past their deadline automatically receive an **additional +50 point bonus**.
- **Completed:** Tasks marked as finished receive a score of **0** and drop out of the priority ranking.

---

## 🇵🇱 Wersja Polska

### 🚀 O projekcie
**TaskIntelligence** to nowoczesna aplikacja full-stack stworzona do optymalizacji codziennej produktywności. W przeciwieństwie do zwykłych list zadań, system posiada wbudowany **silnik Intelligence**, który w czasie rzeczywistym klasyfikuje zadania według ich pilności. Projekt oparty jest o najnowsze standardy: **Spring Boot 4** i **Javę 25**.

### ✨ Główne Funkcje
* 🤖 **Silnik Priorytetów** – Automatyczne wyliczanie punktów ważności zadań.
* 🔐 **Bezpieczeństwo JWT** – Bezstanowa autoryzacja (rejestracja/logowanie).
* 📅 **Harmonogram zadań** – Usługa w tle, która co godzinę aktualizuje rankingi.
* 📄 **REST API** – Przejrzyste endpointy z pełną paginacją i dokumentacją Swagger.
* 🎨 **Frontend w Angularze** – Nowoczesny interfejs (obecnie w fazie przygotowania szkieletu).

### 🧪 Logika Intelligence
Silnik priorytetów zaimplementowany w `TaskPriorityService` wylicza wynik ($S$) według wzoru:

$$S = \frac{Ważność \times 10}{\max(1, DniDoDeadline)}$$

**Zasady działania:**
- **Standard:** Wynik rośnie, im bliżej jest do wyznaczonego terminu.
- **Zaległe:** Zadania po terminie otrzymują automatyczny **bonus +50 punktów**, aby trafić na szczyt listy.
- **Ukończone:** Zadania ze statusem "Completed" mają wynik **0** i wypadają z rankingu.

---

## 🛠 Tech Stack / Technologie

| Component | Technology |
| :--- | :--- |
| **Backend** | Java 25, Spring Boot 4.0.3, Hibernate |
| **Frontend** | Angular 21.2.2 (WIP), Tailwind CSS |
| **Database** | PostgreSQL |
| **Security** | Spring Security + JJWT 0.12 |
| **API Docs** | Springdoc OpenAPI 3.1 |

---

>[!TIP]
Access interactive API docs at: http://localhost:8080/swagger-ui/index.html after running a project

>[!NOTE]
The Frontend application is currently in its initial setup phase. Integration with the Backend API is under active development.

---

Developed with ❤️ by m4tiii
