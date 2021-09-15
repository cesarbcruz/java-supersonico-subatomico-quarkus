package br.com.alura;

import io.quarkus.test.junit.QuarkusTest;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import br.com.alura.model.Usuario;
import br.com.alura.repository.UsuarioRepository;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class UsuarioResourceTest {

    @Inject
    UsuarioRepository usuarioRepository;
    
    @Test
    @Transactional
    public void testUsuariosEndpoint() {
        Jsonb jsonb = JsonbBuilder.create();
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("User Teste");
        novoUsuario.setUserName("teste");
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
        assertEquals(novoUsuario.getPassword(), usuarioPersistido.getPassword());

        usuarioRepository.deleteById(usuarioPersistido.getId());
    }

}