package br.com.alura.resource;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import br.com.alura.model.Usuario;
import br.com.alura.repository.UsuarioRepository;

@Path("/usuarios")
public class UsuarioResource {

    @Inject
    UsuarioRepository usuarioRepository;

    @POST
    @PermitAll
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void inserir(Usuario usuario){
        usuarioRepository.adicionar(usuario);
    }
    
}
