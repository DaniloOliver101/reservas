package br.com.reservas_api.infra.security;

import br.com.reservas_api.controller.request.LoginRequestDTO;
import br.com.reservas_api.controller.request.RegisterRequestDTO;
import br.com.reservas_api.controller.response.ResponseDTO;
import br.com.reservas_api.domain.Role;
import br.com.reservas_api.domain.User;
import br.com.reservas_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final TokenService tokenService;


    public ResponseDTO login(LoginRequestDTO body) {
        var user = repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return new ResponseDTO(user.getName(), token);
        }
        throw new RuntimeException("Invalid credentials");
    }

    public ResponseDTO register(RegisterRequestDTO body) {
        Optional<User> existingUser = repository.findByEmail(body.email());

        if (existingUser.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            newUser.setRole(Role.valueOf(body.role()));

            repository.save(newUser);

            String token = tokenService.generateToken(newUser);
            return new ResponseDTO(newUser.getName(), token);
        }
        throw new RuntimeException("User already exists");
    }
}
