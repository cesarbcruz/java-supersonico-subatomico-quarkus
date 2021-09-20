package br.com.alura.repository;

import javax.enterprise.context.ApplicationScoped;

import br.com.alura.model.Usuario;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario>{

    public void adicionar(Usuario usuario){
        usuario.setPassword(BcryptUtil.bcryptHash(usuario.getPassword()));
        usuario.setRole("alura".equals(usuario.getNome())?"admin":"user");
        persist(usuario);
    }
    
}
