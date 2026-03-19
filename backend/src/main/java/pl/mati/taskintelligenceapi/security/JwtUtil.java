package pl.mati.taskintelligenceapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Klasa narzędziowa (tzw. "Fabryka Tokenów").
 * Oznaczona jako @Service, co pozwala Springowi na utworzenie jej jednej instancji (Singleton)
 * i wstrzykiwanie jej tam, gdzie będzie potrzebna (np. w kontrolerze logowania czy filtrze).
 */
@Service
public class JwtUtil {
    // Tajny klucz używany do podpisywania tokenów. Zabezpiecza token przed sfałszowaniem.
    // UWAGA: W prawdziwych aplikacjach (produkcyjnych) tego klucza NIGDY nie trzyma się w kodzie.
    // Powinien być wstrzykiwany z pliku application.yml (np. przez @Value).
    private static final String SECRET_CUSTOM = "qwertyuiopasdfghjklzxcvbnm1234567890";
    
    // Czas życia tokena w milisekundach.
    private static final long EXPIRATION_TIME = 1000*60*30;
    private static final long REFRESH_EXPIRATION_TIME = 1000*60*60*24*7; //

    /**
     * Metoda przygotowująca klucz kryptograficzny w formacie wymaganym przez bibliotekę JJWT.
     * Zamienia nasz zwykły tekst (String) na tablicę bajtów, a następnie generuje z niego
     * specjalny obiekt SecretKey wykorzystując algorytm HMAC-SHA.
     */
    private SecretKey getSigningKey(){
        byte[] keyBytes = SECRET_CUSTOM.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Główna metoda do tworzenia nowego tokena dla zalogowanego użytkownika.
     */
    public String generateToken(String username){
        return Jwts.builder()
                .subject(username) // Wrzucamy nazwę użytkownika (login) jako "Temat" (Subject) tokena
                .issuedAt(new Date(System.currentTimeMillis())) // Data i czas wydania tokena (teraz)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Data wygaśnięcia (teraz + 10 godzin)
                .signWith(getSigningKey()) // Cyfrowy podpis tokena z użyciem naszego tajnego klucza
                .compact(); // Złożenie tego wszystkiego w jeden długi String (ten, który widzi klient)
    }

    public String generateRefreshToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Metoda pomocnicza wyciągająca konkretnie nazwę użytkownika (Subject) z przekazanego tokena.
     */
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Metoda, z której będzie korzystał nasz "Ochroniarz" (Filtr Security),
     * aby sprawdzić czy token przysłany przez klienta jest na 100% poprawny.
     */
    public boolean isTokenValid(String token, String userDetailUsername){
        final String username = extractUsername(token);
        // Token jest ważny tylko wtedy, gdy login z tokena zgadza się z loginem w naszej bazie (userDetailUsername)
        // ORAZ gdy czas ważności tokena jeszcze nie upłynął.
        return (username.equals(userDetailUsername) && !isTokenExpired(token));
    }

    /**
     * Sprawdza, czy data wygaśnięcia zapisana w tokenie jest "przed" obecnym czasem na serwerze.
     */
    private boolean isTokenExpired(String token){
        return  extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * To najbardziej uniwersalna (i skomplikowana) metoda w tej klasie.
     * Otwiera ("rozszyfrowuje") nadesłany token, sprawdza jego autentyczność za pomocą naszego klucza
     * i wyciąga z niego tak zwane "Claims" (czyli dane, które w nim zapisaliśmy, np. subject, expiration).
     * Dzięki parametrowi Function<Claims, T> pozwala na wyciągnięcie DOWOLNEJ informacji z tokena.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey()) // "Sprawdź, czy nikt nie podrobił tokena, używając mojego klucza"
                .build() // Zbuduj parser
                .parseSignedClaims(token) // Otwórz i odczytaj podpisany token
                .getPayload(); // Pobierz jego "wnętrzności" (czyli Claims)
        
        return claimsResolver.apply(claims); // Zastosuj przekazaną funkcję (np. pobierz Subject) na tych wnętrznościach
    }
}
