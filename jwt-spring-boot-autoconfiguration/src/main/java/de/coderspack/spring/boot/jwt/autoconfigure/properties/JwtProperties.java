package de.coderspack.spring.boot.jwt.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "jwt")
@Validated
public class JwtProperties {

   @NotBlank
   private final String base64Secret;

   @NotBlank
   private final String header;

   private final long tokenValidityInSeconds;

   private final long tokenValidityInSecondsForRememberMe;

   @ConstructorBinding
   public JwtProperties(@NotBlank String base64Secret,
                        @DefaultValue("Authorization") String header,
                        @DefaultValue("86400") long tokenValidityInSeconds,
                        @DefaultValue("108000") long tokenValidityInSecondsForRememberMe) {
      this.base64Secret = base64Secret;
      this.header = header;
      this.tokenValidityInSeconds = tokenValidityInSeconds;
      this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
   }

   public String getBase64Secret() {
      return base64Secret;
   }

   public String getHeader() {
      return header;
   }

   public long getTokenValidityInSeconds() {
      return tokenValidityInSeconds;
   }

   public long getTokenValidityInSecondsForRememberMe() {
      return tokenValidityInSecondsForRememberMe;
   }

}
