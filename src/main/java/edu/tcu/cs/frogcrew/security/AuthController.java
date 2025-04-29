package edu.tcu.cs.frogcrew.security;

import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("${api.endpoint.base-url}/auth")
public class AuthController {

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest) {
        if ("admin@example.com".equals(loginRequest.getUsername()) && "admin123".equals(loginRequest.getPassword())) {
            return new Result(true, StatusCode.SUCCESS, "Login successful", new LoginResponse("mock-token", "admin"));
        } else if ("crew@example.com".equals(loginRequest.getUsername()) && "crew123".equals(loginRequest.getPassword())) {
            return new Result(true, StatusCode.SUCCESS, "Login successful", new LoginResponse("mock-token", "crew"));
        } else {
            // 🛠 THROW 401 Unauthorized error correctly
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
        private String token;
        private String role;

        public LoginResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }

        public String getToken() { return token; }
        public String getRole() { return role; }
    }
}
