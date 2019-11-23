package de.coderspack.spring.boot.jwt.library.security;

import de.coderspack.spring.boot.jwt.library.factory.TokenFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtAuthenticationManager {

   private final AuthenticationManagerBuilder authenticationManagerBuilder;
   private final TokenFactory tokenFactory;

   public JwtAuthenticationManager(final AuthenticationManagerBuilder authenticationManagerBuilder,
                                   final TokenFactory tokenFactory) {
      this.authenticationManagerBuilder = authenticationManagerBuilder;
      this.tokenFactory = tokenFactory;
   }

   public String authenticate(final String username, final String password, final boolean rememberMe) {
      final var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
      final var authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

      SecurityContextHolder.getContext().setAuthentication(authentication);

      return tokenFactory.createToken(authentication, rememberMe);
   }
}
