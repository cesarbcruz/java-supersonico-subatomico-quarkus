package br.com.alura;

import io.quarkus.test.junit.QuarkusTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItem;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class BitcoinsResourceTest {

    @Test
    public void testBitcoinsEndpoint() {
        given()
          .when().get("/bitcoins")
          .then().statusCode(200)
          .body("size()", is(15))
          .body("id", hasItem(1))
          .body("tipo", hasItem("VENDA"))
          .body("data", hasItem("2020-09-15"));
    }

}