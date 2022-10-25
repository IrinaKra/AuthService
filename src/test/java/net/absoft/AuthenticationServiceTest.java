package net.absoft;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.absoft.data.Response;
import net.absoft.services.AuthenticationService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class AuthenticationServiceTest {

  private AuthenticationService authenticationService;

  @BeforeClass(groups = {"positive", "negative"})
  public void setUp() {
    authenticationService = new AuthenticationService();
  }


  @Test(
      description = "Test Successful Authentication",
      groups = "positive"
  )
  public void testSuccessfulAuthentication() throws InterruptedException {
    Response response = authenticationService.authenticate("user1@test.com", "password1");
    SoftAssert sa = new SoftAssert();
    sa.assertEquals(response.getCode(), 200, "Response code should be 200");
    sa.assertTrue(validateToken(response.getMessage()),
        "Token should be the 32 digits string. Got: " + response.getMessage());
    sa.assertAll();

    Thread.sleep(2000);
    System.out.println("testSuccessfulAuthentication: " + new Date());
  }

  @DataProvider(name="invalidLogins", parallel = true)
  public Object[][] invalidAuthenticationData() {
    return new Object[][]{
            new Object[]{"user1@test.com", "wrong_password1",
                    new Response(401, "Invalid email or password")},
            new Object[]{"", "password1",
                    new Response(400, "Email should not be empty string")},
            new Object[]{"user1", "password1",
                    new Response(400, "Invalid email")},
            new Object[]{"user1@test.com", "",
                    new Response(400, "Password should not be empty string")},
    };
  }

  @Test(
          groups = "negative",
          dataProvider = "invalidLogins"
  )
  public void testInvalidAuthentication(String email, String password, Response expectedResponse) throws InterruptedException {
    Response actualResponse = authenticationService
            .authenticate(email, password);
    SoftAssert sa = new SoftAssert();
    sa.assertEquals(actualResponse.getCode(), expectedResponse.getCode(), expectedResponse.getMessage());
    sa.assertEquals(actualResponse.getMessage(), expectedResponse.getMessage(),
            "Response message should be \"Invalid email or password\"");
    sa.assertAll();

    Thread.sleep(2000);
    System.out.println("testSuccessfulAuthentication: " + new Date());
  }


  private boolean validateToken(String token) {
    final Pattern pattern = Pattern.compile("\\S{32}", Pattern.MULTILINE);
    final Matcher matcher = pattern.matcher(token);
    return matcher.matches();
  }
}
