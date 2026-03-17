# 📝 Task Intelligence API - Plan Działania (TODO)

Ten plik służy jako mapa drogowa do samodzielnego zbudowania aplikacji. Odhaczaj (zmieniając `[ ]` na `[x]`) kolejne punkty, gdy uda Ci się je zrealizować!

## Etap 1: Baza Danych i Pierwsza Encja (Fundamenty)
- [x] Upewnij się, że masz uruchomiony lokalny serwer PostgreSQL i utwórz bazę o nazwie `task_intelligence_db`.
- [x] Stwórz encję `Task` w pakiecie `entity`. Powinna mieć pola takie jak: `id` (Long, generowane automatycznie), `title` (String), `description` (String), `completed` (boolean) oraz `createdAt` (LocalDateTime).
- [x] Stwórz interfejs `TaskRepository` w pakiecie `repository`, który rozszerza `JpaRepository<Task, Long>`.
- [x] Uruchom aplikację i sprawdź w pgAdmin/DBeaver, czy tabela `task` wygenerowała się w bazie automatycznie.

## Etap 2: Obiekty Transferu Danych (DTO) i Walidacja
- [x] W pakiecie `dto` stwórz klasę `TaskRequest` (dane przychodzące od klienta, np. tylko `title` i `description`).
- [x] W pakiecie `dto` stwórz klasę `TaskResponse` (dane wysyłane do klienta, ukrywające ewentualne wrażliwe dane z encji).
- [x] Dodaj adnotacje walidacyjne do `TaskRequest` (np. `@NotBlank` nad tytułem).

## Etap 3: Logika Biznesowa (Service)
- [x] Stwórz klasę `TaskService` w pakiecie `service` i oznacz ją adnotacją `@Service`.
- [x] Wstrzyknij `TaskRepository` przez konstruktor (lub użyj `@RequiredArgsConstructor` z Lomboka).
- [x] Napisz metody do: 
  - pobierania wszystkich zadań (zwracające `List<TaskResponse>`),
  - pobierania pojedynczego zadania po `id`,
  - tworzenia nowego zadania (przyjmujące `TaskRequest`),
  - aktualizowania istniejącego zadania,
  - usuwania zadania.

## Etap 4: Kontrolery REST (Wystawienie API w świat)
- [x] Stwórz `TaskController` w pakiecie `controller` i oznacz go `@RestController` oraz `@RequestMapping("/api/tasks")`.
- [x] Wstrzyknij `TaskService`.
- [x] Zaimplementuj endpointy używając `@GetMapping`, `@PostMapping`, `@PutMapping("/{id}")`, `@DeleteMapping("/{id}")`.
- [x] Użyj `@Valid` przy endpointach przyjmujących `TaskRequest`, aby uruchomić walidację.
- [x] Przetestuj wszystkie endpointy w Postmanie, Insomnii lub przez cURL.

## Etap 5: Globalna obsługa błędów (Wyjątki)
- [x] Stwórz własny wyjątek, np. `TaskNotFoundException` w pakiecie `exception`.
- [x] Rzuć ten wyjątek w `TaskService`, gdy ktoś próbuje pobrać/zaktualizować zadanie o nieistniejącym ID.
- [x] Stwórz klasę `GlobalExceptionHandler` z adnotacją `@RestControllerAdvice`.
- [x] Złap swój wyjątek za pomocą `@ExceptionHandler` i zwróć ładny obiekt JSON ze statusem `404 Not Found` zamiast domyślnego błędu Springa (status `500`).

## Etap 6: Użytkownicy i Spring Security (Wyzwanie!)
- [ ] Stwórz encję `User` (id, username, password, rola) oraz `UserRepository`.
- [ ] Połącz `User` z `Task` relacją (np. `@ManyToOne` w encji Task - każde zadanie należy do jakiegoś użytkownika).
- [ ] Stwórz klasę implementującą `UserDetailsService` do ładowania użytkowników z bazy.
- [ ] Utwórz `SecurityConfig` i ustaw szyfrowanie haseł (`PasswordEncoder`).
- [ ] Stwórz `AuthController` do logowania i rejestracji.
- [ ] Zaimplementuj generowanie i walidację tokenów JWT (`JwtUtil`, `JwtFilter`).
- [ ] Skonfiguruj Spring Security tak, by chronić endpointy `/api/tasks` przed niezalogowanymi użytkownikami.

## Etap 7: Testy
- [ ] Napisz testy jednostkowe dla metod w `TaskService` (możesz użyć biblioteki Mockito do zamockowania repozytorium).
- [ ] Napisz testy kontrolerów z użyciem `@WebMvcTest`.