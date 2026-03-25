package pl.mati.taskintelligenceapi.security;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
public class JwtUtilTest {
    JwtUtil jwtUtil = new JwtUtil("moj-super-tajny-klucz-minimum-32-znaki");

    @Test
    void shouldGenerateTokenAndExtreactUsername(){
        String token = jwtUtil.generateToken("userTest");
        Assertions.assertEquals("userTest", jwtUtil.extractUsername(token));
    }

    @Test
    void shouldReturnTrueForValidToken(){
        String token = jwtUtil.generateToken("userTest");
        Assertions.assertTrue(jwtUtil.isTokenValid(token, "userTest"));
    }

    @Test
    void shouldReturnFalseForInvalidToken(){
        String token = jwtUtil.generateToken("userTest");
        Assertions.assertFalse(jwtUtil.isTokenValid(token, "invalidUser"));
    }

    @Test
    void shouldReturnFalseForTamperedToken(){
        String token = jwtUtil.generateToken("userTest");
        String tampered = token.substring(0, token.length() - 1) + "a"; // Change last character
        Assertions.assertThrows(Exception.class, () -> jwtUtil.isTokenValid(tampered, "userTest"));
    }
}
