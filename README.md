# 🧠 TaskIntelligence API
> **Automated Task Prioritization Engine | Inteligentny system zarządzania zadaniami**

[![Java Version](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-green?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Security](https://img.shields.io/badge/Security-JWT-blue?style=for-the-badge&logo=jsonwebtokens)](https://jwt.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

---

## 🌍 Choose your language / Wybierz język
- [English Version](#-english-version)
- [Wersja Polska](#-wersja-polska)

---

## 🇬🇧 English Version

### 🚀 Overview
**TaskIntelligence API** is a high-performance backend system for smart task management. Built with **Java 25** and **Spring Boot 4.x**, it features an automated priority engine that recalculates task urgency in real-time.

### ✨ Key Features
* 🤖 **Smart Priority Engine** – Automatically calculates `priorityScore` based on importance and deadline.
* 🔐 **Stateless Auth** – Secure access using **JWT (JSON Web Tokens)**.
* 📅 **Task Scheduler** – Background service refreshes all priorities every hour.
* 📄 **Advanced Pagination** – Optimized data fetching via `/api/tasks/page`.

### 🧪 Smart Logic
The priority score ($S$) is calculated using a time-decay algorithm:
$$S = \frac{Importance \times 10}{\max(1, DaysToDeadline)}$$
*Overdue tasks automatically receive a **+50 bonus** to stay on top.*

---

## 🇵🇱 Wersja Polska

### 🚀 O projekcie
**TaskIntelligence API** to zaawansowany backend do zarządzania zadaniami, stworzony w najnowszej **Javie 25**. System posiada wbudowany silnik priorytetyzacji, który automatycznie ocenia, które zadania są najważniejsze "tu i teraz".

### ✨ Główne Funkcje
* 🤖 **Inteligentne Priorytety** – Dynamiczne wyliczanie pola `priorityScore`.
* 🔐 **Bezpieczeństwo** – Pełna implementacja **JWT** (rejestracja i logowanie).
* 📅 **Automatyzacja** – `TaskSchedulerService` co godzinę aktualizuje priorytety w tle.
* 📄 **Paginacja** – Obsługa dużych zbiorów danych przez `Pageable`.

### 🧪 Logika "Intelligence"
$$S = \frac{Ważność \times 10}{\max(1, DniDoDeadline)}$$
*Zadania po terminie dostają automatyczny bonus **+50 punktów**, aby nie umknęły Twojej uwadze.*

---

## 🛠 Tech Stack / Technologie

| Category | Technology |
| :--- | :--- |
| **Language** | Java 25 (Bleeding Edge 🚀) |
| **Framework** | Spring Boot 4.0.3 |
| **Database** | PostgreSQL |
| **Security** | Spring Security + JJWT 0.12 |
| **Documentation** | Springdoc OpenAPI 3.1 |

---

>[!TIP]
Access interactive API docs at: http://localhost:8080/swagger-ui/index.html

---

Developed with ❤️ by m4tiii
