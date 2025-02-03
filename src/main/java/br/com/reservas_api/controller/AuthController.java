package br.com.reservas_api.controller;

import br.com.reservas_api.controller.request.LoginRequestDTO;
import br.com.reservas_api.controller.request.RegisterRequestDTO;
import br.com.reservas_api.controller.response.ResponseDTO;
import br.com.reservas_api.infra.security.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginService loginService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        try {
            return ResponseEntity.ok(loginService.login(body));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), null));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequestDTO body) {
        try {
            return ResponseEntity.ok(loginService.register(body));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), null));
        }
    }
}
