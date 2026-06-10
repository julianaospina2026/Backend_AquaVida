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

    // 🔵 CREAR ROLES
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

    // 🔴 CREAR USUARIOS DE PRUEBA
    @Bean
    CommandLineRunner cargarUsuarios(UsuarioRepository usuarioRepository,
        RolRepository rolRepository) {

        return args -> {

            Rol adminRol = rolRepository.findByNombre("ADMINISTRADOR").orElseThrow();
            Rol operadorRol = rolRepository.findByNombre("OPERADOR").orElseThrow();
            Rol presidenteRol = rolRepository.findByNombre("PRESIDENTE").orElseThrow();
            Rol usuarioRol = rolRepository.findByNombre("USUARIO").orElseThrow();

            // ADMIN
            if (usuarioRepository.findByUsername("admin@gmail.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin@gmail.com");
                admin.setPassword("12345");
                admin.setNombreCompleto("Administrador del sistema");
                admin.setCedula("1001");
                admin.setRol(adminRol);
                admin.setActivo(true);

                usuarioRepository.save(admin);
            }

            // OPERADOR
            if (usuarioRepository.findByUsername("operador@gmail.com").isEmpty()) {
                Usuario operador = new Usuario();
                operador.setUsername("operador@gmail.com");
                operador.setPassword("123456");
                operador.setNombreCompleto("Operador del sistema");
                operador.setCedula("1002");
                operador.setRol(operadorRol);
                operador.setActivo(true);

                usuarioRepository.save(operador);
            }

            // PRESIDENTE
            if (usuarioRepository.findByUsername("presidente@gmail.com").isEmpty()) {
                Usuario presidente = new Usuario();
                presidente.setUsername("presidente@gmail.com");
                presidente.setPassword("1234567");
                presidente.setNombreCompleto("Presidente del sistema");
                presidente.setCedula("1003");
                presidente.setRol(presidenteRol);
                presidente.setActivo(true);

                usuarioRepository.save(presidente);
            }

            // USUARIO NORMAL
            if (usuarioRepository.findByUsername("usuario@gmail.com").isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setUsername("usuario@gmail.com");
                usuario.setPassword("123");
                usuario.setNombreCompleto("Usuario normal");
                usuario.setCedula("10489527469");
                usuario.setRol(usuarioRol);
                usuario.setActivo(true);

                usuarioRepository.save(usuario);
            }
        };
    }
}