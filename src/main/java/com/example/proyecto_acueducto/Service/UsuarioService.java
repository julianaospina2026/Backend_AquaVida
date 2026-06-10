package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Usuario;
import com.example.proyecto_acueducto.Repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario login(String usuario, String password) {
        return usuarioRepository.findByEmailAndPassword(usuario, password)
                .or(() -> usuarioRepository.findByCedulaAndPassword(usuario, password))
                .or(() -> usuarioRepository.findByUsernameAndPassword(usuario, password))
                .orElse(null);
    }

    // 🔥 MÉTODO CLAVE CORREGIDO
    @Transactional
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuario no encontrado con ID: " + id
                ));

        usuario.setUsername(usuarioActualizado.getUsername());
        usuario.setPassword(usuarioActualizado.getPassword());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setCedula(usuarioActualizado.getCedula());
        usuario.setNombreCompleto(usuarioActualizado.getNombreCompleto());
        usuario.setActivo(usuarioActualizado.getActivo());

        // 🔥 SOLO SI VIENE VALIDO
        if (usuarioActualizado.getRol() != null) {
            usuario.setRol(usuarioActualizado.getRol());
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}