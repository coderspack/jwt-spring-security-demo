package de.coderspack.spring.boot.jwt.autoconfigure;

import de.coderspack.spring.boot.jwt.autoconfigure.properties.JwtProperties;
import de.coderspack.spring.boot.jwt.library.security.JwtAuthenticationManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import de.coderspack.spring.boot.jwt.library.JWTFilter;
import de.coderspack.spring.boot.jwt.library.factory.TokenFactory;
import de.coderspack.spring.boot.jwt.library.security.JWTConfigurer;
import de.coderspack.spring.boot.jwt.library.security.web.access.JwtAccessDeniedHandler;
import de.coderspack.spring.boot.jwt.library.security.web.access.JwtAuthenticationEntryPoint;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {

   @Bean
   @ConditionalOnMissingBean
   public TokenFactory tokenProvider(final JwtProperties jwtProperties) {
      return new TokenFactory(jwtProperties.getBase64Secret(), jwtProperties.getTokenValidityInSeconds(), jwtProperties.getTokenValidityInSecondsForRememberMe());
   }

   @Bean
   @ConditionalOnMissingBean
   public JwtAuthenticationManager jwtAuthenticationManager(final AuthenticationManagerBuilder authenticationManagerBuilder, final TokenFactory tokenFactory) {
      return new JwtAuthenticationManager(authenticationManagerBuilder, tokenFactory);
   }

   @Bean
   @ConditionalOnMissingBean
   public JWTFilter jwtFilter(final TokenFactory tokenFactory, final JwtProperties jwtProperties) {
      return new JWTFilter(tokenFactory, jwtProperties.getHeader());
   }

   @Bean
   @ConditionalOnMissingBean
   public JWTConfigurer jwtConfigurer(final TokenFactory tokenFactory, final JwtProperties jwtProperties) {
      return new JWTConfigurer(tokenFactory, jwtProperties.getHeader());
   }

   @Bean
   @ConditionalOnMissingBean
   public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
      return new JwtAuthenticationEntryPoint();
   }

   @Bean
   @ConditionalOnMissingBean
   public JwtAccessDeniedHandler jwtAccessDeniedHandler() {
      return new JwtAccessDeniedHandler();
   }
}
