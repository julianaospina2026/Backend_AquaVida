package com.example.proyecto_acueducto.Repository;

import com.example.proyecto_acueducto.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // 🔹 LOGIN POR USERNAME
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByUsernameAndPassword(String username, String password);

    // 🔹 LOGIN POR EMAIL (ADMIN / OPERADOR / PRESIDENTE)
    Optional<Usuario> findByEmailAndPassword(String email, String password);

    // 🔹 LOGIN POR CÉDULA (SUSCRIPTORES)
    Optional<Usuario> findByCedulaAndPassword(String cedula, String password);

    // (opcional recomendado)
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCedula(String cedula);
}