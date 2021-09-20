package br.com.alura;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.test.junit.QuarkusTest;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.model.Ordem;
import br.com.alura.model.Usuario;
import br.com.alura.repository.OrdemRepository;
import br.com.alura.repository.UsuarioRepository;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class OrdemResourceTest {

    @Inject
    OrdemRepository ordemRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    Usuario usuario;
    Ordem ordem;
    Jsonb jsonb = JsonbBuilder.create();

    @BeforeEach
    @Transactional
    public void setup(){
        usuario = new Usuario();
        usuario.setNome("User Teste");
        usuario.setUserName("teste");
        usuario.setPassword(BcryptUtil.bcryptHash("123456"));
        usuario.setCpf("12345678901");
        usuario.setRole("user");
        usuarioRepository.persist(usuario);
    }

    @Test
    public void testOrdemEndpoint() {
        usuario = usuarioRepository.find("nome", usuario.getNome()).firstResult();
        ordem = new Ordem();
        ordem.setUserId(usuario.getId());
        ordem.setPreco(85.10);
        ordem.setTipo("COMPRA");

        given()
        .body(jsonb.toJson(ordem))
        .auth().basic(usuario.getUserName(), "123456")
        .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
        .post("/ordens")
        .then()
        .statusCode(204);

        ordem = ordemRepository.find("tipo", ordem.getTipo()).firstResult();

        assertNotNull(ordem.getId());
        assertNotNull(ordem.getPreco());
        assertNotNull(ordem.getData());
        assertNotNull(ordem.getStatus());

    }

    @AfterEach
    @Transactional
    public void clear(){
        ordemRepository.deleteById(ordem.getId());
        usuarioRepository.deleteById(usuario.getId());

    }

}