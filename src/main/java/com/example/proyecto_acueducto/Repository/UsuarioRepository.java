package com.example.proyecto_acueducto.Repository;

import com.example.proyecto_acueducto.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCedula(String cedula);

    // 🔥 LOGIN MULTI-IDENTIFICADOR
    Optional<Usuario> findByCedulaOrEmailOrUsername(
            String cedula,
            String email,
            String username
    );
}