package org.zerhusen.sample.app.rest;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.zerhusen.sample.app.util.AbstractRestControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zerhusen.sample.app.util.LogInUtils.getTokenForLogin;

public class PersonRestControllerTest extends AbstractRestControllerTest {

   @Test
   public void getPersonForUser() throws Exception {
      final String token = getTokenForLogin("user", "password", getMockMvc());

      assertSuccessfulPersonRequest(token);
   }

   @Test
   public void getPersonForAdmin() throws Exception {
      final String token = getTokenForLogin("admin", "admin", getMockMvc());

      assertSuccessfulPersonRequest(token);
   }

   @Test
   public void getPersonForAnonymous() throws Exception {
      getMockMvc().perform(get("/api/person")
         .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isUnauthorized());
   }

   private void assertSuccessfulPersonRequest(String token) throws Exception {
      getMockMvc().perform(get("/api/person")
         .contentType(MediaType.APPLICATION_JSON)
         .header("Authorization", "Bearer " + token))
         .andExpect(status().isOk())
         .andExpect(content().json(
            "{\n" +
               "  \"name\" : \"John Doe\",\n" +
               "  \"email\" : \"john.doe@test.org\"\n" +
               "}"
         ));
   }
}
