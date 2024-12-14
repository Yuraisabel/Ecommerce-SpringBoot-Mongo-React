package com.project.backend.controller;

import com.project.backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("")
    public ResponseEntity<?> getAdminResource(@RequestHeader("Authorization") String token) {
        // Extraer el token JWT del encabezado
        String role = jwtUtils.getClaims(token.replace("Bearer ", "")).get("role", String.class);

        // Verificar si el rol es ADMIN
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }

        return ResponseEntity.ok("Recurso para administradores");
    }
}
