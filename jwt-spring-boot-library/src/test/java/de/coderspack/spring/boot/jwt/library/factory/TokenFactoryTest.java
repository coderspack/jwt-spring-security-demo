package de.coderspack.spring.boot.jwt.library.factory;

import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class TokenFactoryTest {

   private static final String SECRET_KEY = "ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=";
   private static final int TOKEN_VALIDITY_IN_SECONDS = 86400;
   private static final int TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME = 108000;
   private final TokenFactory tokenFactory = new TokenFactory(SECRET_KEY, TOKEN_VALIDITY_IN_SECONDS, TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME);

   @Test
   void createTokenWithoutRememberMe() {
      final var authentication = createAuthentication("test-user-name", "ROLE_USER");

      final var token = tokenFactory.createToken(authentication);

      final var claims = parseClaims(token);

      assertThat(claims.get("sub")).isEqualTo("test-user-name");
      assertThat(claims.get("auth")).isEqualTo("ROLE_USER");
      assertThat(claims.getExpiration()).isCloseTo(new Date(System.currentTimeMillis() + (TOKEN_VALIDITY_IN_SECONDS * 1000)), 2000);
   }

   @Test
   void createTokenWithRememberMeIsTrue() {
      final var authentication = createAuthentication("test-other-name", "ROLE_ADMIN");

      final var token = tokenFactory.createToken(authentication, true);

      final var claims = parseClaims(token);

      assertThat(claims.get("sub")).isEqualTo("test-other-name");
      assertThat(claims.get("auth")).isEqualTo("ROLE_ADMIN");
      assertThat(claims.getExpiration()).isCloseTo(new Date(System.currentTimeMillis() + (TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME * 1000)), 2000);
   }

   @Test
   void createTokenWithRememberMeIsFalse() {
      final var authentication = createAuthentication("test-other-name", "ROLE_ADMIN", "ROLE_USER");

      final var token = tokenFactory.createToken(authentication, false);

      final var claims = parseClaims(token);

      assertThat(claims.get("sub")).isEqualTo("test-other-name");
      assertThat(claims.get("auth")).isEqualTo("ROLE_ADMIN,ROLE_USER");
      assertThat(claims.getExpiration()).isCloseTo(new Date(System.currentTimeMillis() + (TOKEN_VALIDITY_IN_SECONDS * 1000)), 2000);
   }

   @Test
   void createTokenWithoutAuthorities() {
      final var authentication = createAuthentication("test-user-name-no-authorities");

      final var token = tokenFactory.createToken(authentication, true);

      final var claims = parseClaims(token);

      assertThat(claims.get("sub")).isEqualTo("test-user-name-no-authorities");
      assertThat(claims.get("auth")).isEqualTo("");
      assertThat(claims.getExpiration()).isCloseTo(new Date(System.currentTimeMillis() + (TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME * 1000)), 2000);
   }

   private DefaultClaims parseClaims(final String token) {
      return (DefaultClaims) Jwts.parser()
         .setSigningKey(SECRET_KEY)
         .parseClaimsJws(token)
         .getBody();
   }

   private Authentication createAuthentication(final String username, final String... authorities) {
      final var principal = new UserPrincipal(username);
      return new TestingAuthenticationToken(principal, null, authorities);
   }
}
