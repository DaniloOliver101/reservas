package br.com.reservas_api.controller.request;

public record RegisterRequestDTO(String name, String email, String password, String role) {
    public RegisterRequestDTO(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = (role == null || role.isBlank()) ? "user" : role;
    }
}
