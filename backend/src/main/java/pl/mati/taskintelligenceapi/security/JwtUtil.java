package pl.mati.taskintelligenceapi.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "moj-super-tajny-klucz-minimum-32-znaki"
            .getBytes(StandardCharsets.UTF_8)
    );
    private final Long EXPIRATION = 1000*60*15L;
    private final Long REFRESH_EXPIRATION = 1000*60*60*24*7L;

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }



    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, String username){
        try{
            if(extractUsername(token).equals(username)) return true;
        }catch (JwtException e){
            throw e;
        }
        return false;
    }
}
