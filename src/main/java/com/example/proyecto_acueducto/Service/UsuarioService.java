package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Usuario;
import com.example.proyecto_acueducto.Repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // =========================
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // =========================
    public Optional<Usuario> login(String identificador, String password) {

        if (identificador == null || password == null) {
            return Optional.empty();
        }

        Optional<Usuario> usuarioOpt = usuarioRepository
                .findByCedulaOrEmailOrUsername(
                        identificador,
                        identificador,
                        identificador
                );

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getPassword() == null) {
            return Optional.empty();
        }

        return passwordEncoder.matches(password, usuario.getPassword())
                ? Optional.of(usuario)
                : Optional.empty();
    }

    // =========================
    @Transactional
    public Usuario guardar(Usuario usuario) {

        if (usuario.getPassword() != null &&
                !usuario.getPassword().startsWith("$2a$")) {

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    // =========================
    @Transactional
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuario.setUsername(usuarioActualizado.getUsername());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setCedula(usuarioActualizado.getCedula());
        usuario.setNombreCompleto(usuarioActualizado.getNombreCompleto());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setActivo(usuarioActualizado.getActivo());

        if (usuarioActualizado.getPassword() != null &&
                !usuarioActualizado.getPassword().startsWith("$2a$")) {

            usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        usuario.setRol(usuarioActualizado.getRol());
        usuario.setCliente(usuarioActualizado.getCliente());

        return usuarioRepository.save(usuario);
    }

    // =========================
    @Transactional
    public void eliminar(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
    }
}