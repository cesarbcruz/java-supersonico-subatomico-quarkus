package br.com.alura.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.SecurityContext;

import br.com.alura.model.Ordem;
import br.com.alura.model.Usuario;
import br.com.alura.repository.OrdemRepository;
import br.com.alura.repository.UsuarioRepository;

@ApplicationScoped
public class OrdemService {

    @Inject
    OrdemRepository ordemRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Transactional
    public void inserir(SecurityContext securityContext, Ordem ordem) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByIdOptional(ordem.getUserId());
        Usuario usuarioOrdem = usuarioOptional.orElseThrow();

        if(!usuarioOrdem.getUserName().equals(securityContext.getUserPrincipal().getName())){
            throw new RuntimeException("O usuário autenticado é diferente do usuário da ordem");
        }

        ordem.setData(LocalDate.now());
        ordem.setStatus("ENVIADA");
        ordemRepository.persist(ordem);
    }

    public List<Ordem> listarTodas() {
        return ordemRepository.listAll();
    }
    
}
