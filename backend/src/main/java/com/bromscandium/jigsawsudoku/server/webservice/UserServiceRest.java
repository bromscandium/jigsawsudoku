package com.bromscandium.jigsawsudoku.server.webservice;

import com.bromscandium.jigsawsudoku.server.requests.AuthenticationRequest;
import com.bromscandium.jigsawsudoku.entity.User;
import com.bromscandium.jigsawsudoku.interfaces.UserInterface;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class UserServiceRest {

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authRequest) {
        Optional<User> userOpt = userInterface.findByLogin(authRequest.getLogin());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(authRequest.getPassword(), user.getPasswordHash())) {
                String token = UUID.randomUUID().toString();
                user.setSessionToken(token);
                userInterface.save(user);

                ResponseCookie cookie = ResponseCookie.from("session_token", token)
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("None")
                        .path("/")
                        .maxAge(7 * 24 * 60 * 60)
                        .build();

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(Map.of(
                                "message", "Login successful!",
                                "nickname", user.getNickname()
                        ));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "message", "Invalid login or password"
        ));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "player".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authenticated!"));
        }
        return ResponseEntity.ok(Map.of("message", "Authenticated!"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateNickname(@RequestParam String oldNickname, @RequestParam String newNickname) {
        Optional<User> user = userInterface.findByNickname(oldNickname);

        if (user.isPresent()) {
            userInterface.updateNickname(oldNickname, newNickname);
            return ResponseEntity.ok(Map.of("message", "Nickname updated successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found!"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("session_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully!"));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authRequest, HttpServletResponse response) {
        boolean nicknameTaken = userInterface.existsByNickname(authRequest.getNickname());
        boolean loginTaken = userInterface.existsByLogin(authRequest.getLogin());

        if (nicknameTaken) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "errorCode", 1,
                    "message", "Nickname already taken"
            ));
        }

        if (loginTaken) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "errorCode", 2,
                    "message", "Login already taken"
            ));
        }

        Optional<User> user = userInterface.register(
                authRequest.getNickname(),
                authRequest.getLogin(),
                authRequest.getPassword()
        );

        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            user.get().setSessionToken(token);
            userInterface.save(user.get());

            Cookie cookie = createCookie(token);
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("message", "Registration successful!"));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "errorCode", 0,
                "message", "Unknown registration error"
        ));
    }


    private String generateTokenFor(User user) {
        return UUID.randomUUID().toString();
    }

    private Cookie createCookie(String token) {
        Cookie cookie = new Cookie("session_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        return cookie;
    }
}
