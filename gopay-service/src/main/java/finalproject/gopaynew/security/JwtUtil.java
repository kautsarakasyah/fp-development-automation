package finalproject.gopaynew.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "ini-adalah-kunci-rahasia-gopay-yang-sangat-aman-dan-panjang";
    private final Key signingKey;

    public JwtUtil() {
        // Kunci rahasia harus langsung dikonversi ke byte, tanpa Base64 encoding.
        byte[] keyBytes = SECRET_KEY.getBytes();
        this.signingKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String extractPartner(String token) {
        return extractClaim(token, claims -> claims.get("partner", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        try {
            final String partner = extractPartner(token);
            // Validasi sederhana: partner harus "BNI_MOBILE" dan token tidak kedaluwarsa
            return ("BNI_MOBILE".equals(partner) && !isTokenExpired(token));
        } catch (Exception e) {
            // Jika ada error saat parsing, token tidak valid
            return false;
        }
    }

    // Method untuk membuat token (tidak digunakan di service ini, tapi bagus untuk ada)
    public String generateToken(String partner) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("partner", partner);
        return createToken(claims, partner);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 jam
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
