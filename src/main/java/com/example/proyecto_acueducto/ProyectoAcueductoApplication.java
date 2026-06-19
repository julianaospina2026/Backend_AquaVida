package com.example.proyecto_acueducto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.proyecto_acueducto.Model.Rol;
import com.example.proyecto_acueducto.Model.Usuario;
import com.example.proyecto_acueducto.Repository.RolRepository;
import com.example.proyecto_acueducto.Repository.UsuarioRepository;

@SpringBootApplication
public class ProyectoAcueductoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProyectoAcueductoApplication.class, args);
    }

    // =========================
    // CREAR ROLES
    // =========================
    @Bean
    CommandLineRunner cargarRoles(RolRepository rolRepository) {
        return args -> {

            crearRolSiNoExiste(rolRepository, "ADMINISTRADOR");
            crearRolSiNoExiste(rolRepository, "OPERADOR");
            crearRolSiNoExiste(rolRepository, "PRESIDENTE");
            crearRolSiNoExiste(rolRepository, "USUARIO");
        };
    }

    private void crearRolSiNoExiste(RolRepository rolRepository, String nombre) {

        if (rolRepository.findByNombre(nombre).isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rolRepository.save(rol);
        }
    }

    // =========================
    // CREAR USUARIOS DE PRUEBA
    // =========================
    @Bean
    CommandLineRunner cargarUsuarios(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository) {

        return args -> {

            Rol adminRol = rolRepository.findByNombre("ADMINISTRADOR").orElseThrow();
            Rol operadorRol = rolRepository.findByNombre("OPERADOR").orElseThrow();
            Rol presidenteRol = rolRepository.findByNombre("PRESIDENTE").orElseThrow();
            Rol usuarioRol = rolRepository.findByNombre("USUARIO").orElseThrow();

            // ADMIN
            crearUsuarioSiNoExiste(usuarioRepository,
                    "admin@gmail.com",
                    "12345",
                    "Administrador del sistema",
                    "1001",
                    adminRol);

            // OPERADOR
            crearUsuarioSiNoExiste(usuarioRepository,
                    "operador@gmail.com",
                    "123456",
                    "Operador del sistema",
                    "1002",
                    operadorRol);

            // PRESIDENTE
            crearUsuarioSiNoExiste(usuarioRepository,
                    "presidente@gmail.com",
                    "1234567",
                    "Presidente del sistema",
                    "1003",
                    presidenteRol);

            // USUARIO NORMAL
            crearUsuarioSiNoExiste(usuarioRepository,
                    "usuario@gmail.com",
                    "123",
                    "Usuario normal",
                    "10489527469",
                    usuarioRol);
        };
    }

    private void crearUsuarioSiNoExiste(
            UsuarioRepository usuarioRepository,
            String username,
            String password,
            String nombreCompleto,
            String cedula,
            Rol rol) {

        if (usuarioRepository.findByUsername(username).isEmpty()) {

            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(password);
            usuario.setNombreCompleto(nombreCompleto);
            usuario.setCedula(cedula);
            usuario.setRol(rol);
            usuario.setActivo(true);

            usuarioRepository.save(usuario);
        }
    }
}