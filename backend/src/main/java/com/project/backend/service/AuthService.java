package com.project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.backend.model.User;
import com.project.backend.repository.UserRepository;
import com.project.backend.security.JwtUtils;


import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // Registrar usuario
    public String registerUser(String name, String email, String phone, String password, String address) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }
    
        // Crear usuario
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setPassword(passwordEncoder.encode(password)); // Encriptar contraseña
        newUser.setAddress(address);
        newUser.setRole("USER"); // Asignar rol predeterminado
    
        // Guardar usuario
        userRepository.save(newUser);
        return "Usuario registrado con éxito.";
    }
    
    

    // Autenticar usuario
    public String authenticateUser(String email, String password) {
        System.out.println("Iniciando autenticación para el correo: " + email);
    
        // Buscar usuario por correo
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            System.out.println("Usuario no encontrado.");
            throw new IllegalArgumentException("Credenciales incorrectas.");
        }
    
        User user = userOptional.get();
        System.out.println("Usuario encontrado: " + user.getName());
    
        // Verificar la contraseña
        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Contraseña incorrecta.");
            throw new IllegalArgumentException("Credenciales incorrectas.");
        }
    
        // Generar el token JWT
        try {
            System.out.println("Generando token JWT...");
            String token = jwtUtils.generateToken(user);
            System.out.println("Token generado: " + token);
            return token;
        } catch (Exception e) {
            System.out.println("Error al generar el token: " + e.getMessage());
            throw new RuntimeException("Error interno al generar el token.");
        }
    }
    
    
}
