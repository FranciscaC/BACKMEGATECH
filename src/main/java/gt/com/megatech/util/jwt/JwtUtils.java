package gt.com.megatech.util.jwt;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {
    @Value("${security.jwt.secret.key}")
    private String secretKey;

    @Value("${security.jwt.secret.user}")
    private String secretUser;

    public String createToken(
            Authentication authentication
    ) {
        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        String username = authentication.getPrincipal().toString();
        String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return JWT
                .create()
                .withIssuer(this.secretUser)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 64800000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public DecodedJWT validatedToken(
            String token
    ) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
            JWTVerifier jwtVerifier = JWT
                    .require(algorithm)
                    .withIssuer(this.secretUser)
                    .build();
            return jwtVerifier.verify(token);
        } catch (JWTVerificationException jwtVerificationException) {
            throw new JWTVerificationException("Token invalid, not Authorized");
        }
    }

    public String extractUsername(
            DecodedJWT decodedJWT
    ) {
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(
            DecodedJWT decodedJWT,
            String claimName
    ) {
        return decodedJWT.getClaim(claimName);
    }
}
