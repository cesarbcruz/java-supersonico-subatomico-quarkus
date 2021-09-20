package br.com.alura;

import io.quarkus.test.junit.QuarkusTest;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.WildFlyElytronPasswordProvider;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;

import br.com.alura.model.Usuario;
import br.com.alura.repository.UsuarioRepository;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItem;

@QuarkusTest
public class UsuarioResourceTest {

    @Inject
    UsuarioRepository usuarioRepository;
    
    @Test
    @Transactional
    public void testUsuariosEndpoint() throws Exception {
        Jsonb jsonb = JsonbBuilder.create();
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("User Teste Amind");
        novoUsuario.setUserName("alura");
        novoUsuario.setPassword("123456");
        novoUsuario.setCpf("12345678901");
        given()
        .body(jsonb.toJson(novoUsuario))
        .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
        .post("/usuarios")
        .then()
        .statusCode(204);

        Usuario usuarioPersistido = usuarioRepository.find("nome", novoUsuario.getNome()).firstResult();
        assertNotNull(novoUsuario.getCpf(), usuarioPersistido.getCpf());
        assertEquals(novoUsuario.getUserName(), usuarioPersistido.getUserName());
       
        assertTrue(verifyBCryptPassword(usuarioPersistido.getPassword(), novoUsuario.getPassword()));

        given()
        .auth().basic(novoUsuario.getUserName(), novoUsuario.getPassword())
        .when()
        .get("/usuarios")
        .then()
        .statusCode(200)
        .body("size()", is(1))
          .body("nome", hasItem(novoUsuario.getNome()))
          .body("cpf", hasItem(novoUsuario.getCpf()))
          .body("userName", hasItem(novoUsuario.getUserName()))
          .body("role", hasItem("admin"));


        usuarioRepository.deleteAll();
    }


    public static boolean verifyBCryptPassword(String bCryptPasswordHash, String passwordToVerify) throws Exception {

        WildFlyElytronPasswordProvider provider = new WildFlyElytronPasswordProvider();

        // 1. Create a BCrypt Password Factory
        PasswordFactory passwordFactory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT, provider);

        // 2. Decode the hashed user password
        Password userPasswordDecoded = ModularCrypt.decode(bCryptPasswordHash);

        // 3. Translate the decoded user password object to one which is consumable by this factory.
        Password userPasswordRestored = passwordFactory.translate(userPasswordDecoded);

        // Verify existing user password you want to verify
        return passwordFactory.verify(userPasswordRestored, passwordToVerify.toCharArray());

    }

}