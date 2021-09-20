package br.com.alura.resource;

import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import br.com.alura.model.Usuario;
import br.com.alura.model.UsuarioResponse;
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

    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public java.util.List<UsuarioResponse> getAll(){
        return usuarioRepository.listAll().stream().map(usuario -> new UsuarioResponse(usuario)).collect(Collectors.toList());
    }
    
}
