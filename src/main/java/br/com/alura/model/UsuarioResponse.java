package br.com.alura.model;

import javax.json.bind.annotation.JsonbTransient;

public class UsuarioResponse extends Usuario {

    public UsuarioResponse(Usuario usuario){
        setId(usuario.getId());
        setNome(usuario.getNome());
        setCpf(usuario.getCpf());
        setRole(usuario.getRole());
        setUserName(usuario.getUserName());
    }

    @Override
    @JsonbTransient
    public String getPassword() {
        return super.getPassword();
    }
    
}
