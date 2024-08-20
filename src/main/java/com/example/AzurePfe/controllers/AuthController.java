package com.example.AzurePfe.controllers;

import com.example.AzurePfe.ApplicationContextProvider;
import com.example.AzurePfe.models.*;
import com.example.AzurePfe.repository.RoleRepository;
import com.example.AzurePfe.repository.UserRepository;
import com.example.AzurePfe.security.jwt.JwtUtils;
import com.example.AzurePfe.security.services.EmailService;
import com.example.AzurePfe.security.services.UserDetailsImpl;
import com.example.AzurePfe.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;



import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;


import com.example.AzurePfe.payload.request.LoginRequest;
import com.example.AzurePfe.payload.request.SignupRequest;
import com.example.AzurePfe.payload.response.JwtResponse;
import com.example.AzurePfe.payload.response.MessageResponse;


//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private EmailService emailService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @GetMapping("/totalUsers")
    public ResponseEntity<Long> getTotalUsers() {
        long totalUsers = userDetailsService.getTotalUsers();
        return ResponseEntity.ok(totalUsers);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
    @GetMapping("/email")
    public ResponseEntity<?> getEmailByUsername(@RequestParam String username) {
        Optional<String> emailOptional = userDetailsService.getEmailByUsername(username);
        if (emailOptional.isPresent()) {
            return ResponseEntity.ok(emailOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;

                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }



    @PutMapping("/modifierPassword/{username}")
    public ResponseEntity<?> updatePassword(@PathVariable("username") String username, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        try {
            // Vérifier que le nouveau mot de passe correspond à la confirmation
            if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: New password and confirmation password do not match."));
            }

            // Récupérer le PasswordEncoder à partir du contexte Spring
            PasswordEncoder passwordEncoder = getPasswordEncoder();

            // Mettre à jour le mot de passe de l'utilisateur
            userDetailsService.updatePassword(username, updatePasswordRequest.getCurrentPassword(), updatePasswordRequest.getNewPassword(), passwordEncoder);
            return ResponseEntity.ok(new MessageResponse("Password updated successfully!"));
        } catch (UsernameNotFoundException e) {
            // Gérer l'erreur si l'utilisateur n'est pas trouvé
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User not found."));
        } catch (BadCredentialsException e) {
            // Gérer l'erreur si le mot de passe actuel est incorrect
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Incorrect current password."));
        }
    }
    // Méthode pour récupérer le PasswordEncoder à partir du contexte Spring
    private PasswordEncoder getPasswordEncoder() {
        return ApplicationContextProvider.getBean(PasswordEncoder.class);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is required."));
        }

        try {
            // Récupérer l'utilisateur à partir de l'e-mail
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

            // Générer un token de réinitialisation de mot de passe
            userDetailsService.generateResetPasswordToken(email);

            // Construire le lien de réinitialisation de mot de passe
            String resetPasswordLink = "http://localhost:4200/resetPassword?resetToken=" + userDetailsService.getResetTokenByEmail(email) + "&email=" + email;

            String emailSubject = "Réinitialisation de mot de passe";
            String emailBody = "Bonjour, \n\n" +
                    "Vous avez demandé à réinitialiser votre mot de passe. Veuillez cliquer sur le lien suivant pour réinitialiser votre mot de passe : \n" +
                    resetPasswordLink + "\n\n" +
                    "Cordialement,\nVotre équipe.";

            // Envoyer l'e-mail de réinitialisation de mot de passe
            emailService.sendEmail(email, emailSubject, emailBody);

            return ResponseEntity.ok(new MessageResponse("Un e-mail de réinitialisation de mot de passe a été envoyé à votre adresse e-mail enregistrée."));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found with this email."));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> resetRequest) {
        String password = resetRequest.get("password");
        String confirmPassword = resetRequest.get("confirmPassword"); // New line added

        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Password is required."));
        }

        if (!password.equals(confirmPassword)) { // New lines added
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Passwords do not match."));
        }

        try {
            String email = resetRequest.get("email");
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is required."));
            }

            String resetToken = resetRequest.get("resetToken");
            if (resetToken == null || resetToken.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Reset token is required."));
            }

            // Récupérer le resetToken à partir de la base de données
            String userResetToken = userDetailsService.getResetTokenByEmail(email);

            // Vérifier si le resetToken fourni correspond à celui récupéré de la base de données
            if (userResetToken == null || !userResetToken.equals(resetToken)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid reset token."));
            }

            // Récupérer l'utilisateur à partir de l'e-mail
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

            // Hacher le nouveau mot de passe
            String hashedPassword = encoder.encode(password);

            // Mettre à jour le mot de passe de l'utilisateur
            user.setPassword(hashedPassword);
            user.setResetToken(null);
            userRepository.save(user); // Enregistrement en base MongoDB

            // Envoyer un email de confirmation
            String emailSubject = "Confirmation de réinitialisation de mot de passe";
            String emailBody = "Bonjour, \n\n" +
                    "Votre mot de passe a été réinitialisé avec succès. Veuillez vous connecter avec votre nouveau mot de passe.\n\n" +
                    "Cordialement,\nVotre équipe.";
            emailService.sendEmail(email, emailSubject, emailBody);

            return ResponseEntity.ok(new MessageResponse("Votre mot de passe a été réinitialisé avec succès."));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found with this email."));
        }
    }


    @GetMapping("/token/{email}")
    public ResponseEntity<?> getResetTokenByEmail(@PathVariable String email) {
        try {
            String resetToken = userDetailsService.getResetTokenByEmail(email);
            return ResponseEntity.ok(resetToken);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("User Not Found with email: " + email);
        }
    }



}










