# Configuração de Autenticação com Spring Security

## Passo 1: Adicionar dependências

Adicione as seguintes dependências no seu arquivo `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```
Essas dependências adicionam o suporte ao Spring Security no seu projeto e permitem que você escreva testes de segurança.

## Passo 2: Configurar a classe de segurança

Crie uma classe de configuração de segurança que implemente `SecurityFilterChain`:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF para permitir chamadas de API
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/public/**").permitAll() 
                .anyRequest().authenticated() 
            )
            .addFilterBefore(new CustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Adiciona filtro de autenticação customizado

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Define o codificador de senha como BCrypt
    }
}
```
- `csrf(csrf -> csrf.disable())`: Desabilita a proteção CSRF para permitir chamadas de API.
- `addFilterBefore`: Adiciona um filtro de autenticação customizado antes do filtro padrão de autenticação.

## Passo 3: Configurar o filtro de autenticação customizado

Crie um filtro de autenticação customizado para processar as credenciais de login via API:

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Map<String, String> credentials = new ObjectMapper().readValue(request.getInputStream(), HashMap.class);
            String username = credentials.get("username");
            String password = credentials.get("password");
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Login successful");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Login failed");
    }
}
```
- `CustomAuthenticationFilter`: Filtro customizado para processar autenticação via API.
- `attemptAuthentication`: Processa as credenciais de login enviadas na requisição.
- `successfulAuthentication`: Responde com sucesso em caso de autenticação bem-sucedida.
- `unsuccessfulAuthentication`: Responde com falha em caso de autenticação mal-sucedida.

## Passo 4: Configurar a classe de serviço de usuário

Crie uma classe que implemente `UserDetailsService` para carregar os detalhes do usuário:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
```
- `UserDetailsService`: Interface usada para recuperar dados do usuário.
- `loadUserByUsername`: Método que carrega o usuário pelo nome de usuário.

## Passo 5: Configurar a entidade de usuário

Certifique-se de que sua entidade de usuário tenha os campos necessários:

```java
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private Long id;
    private String username;
    private String password;
    // getters e setters
}
```
- `User`: Entidade JPA que representa um usuário no banco de dados.

## Passo 6: Configurar o repositório de usuário

Crie um repositório para a entidade de usuário:

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
```
- `UserRepository`: Interface de repositório JPA para acessar dados do usuário.

## Passo 7: Configurar a página de login

Crie uma página de login simples em `src/main/resources/templates/login.html`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <form th:action="@{/login}" method="post">
        <div>
            <label>Username:</label>
            <input type="text" name="username"/>
        </div>
        <div>
            <label>Password:</label>
            <input type="password" name="password"/>
        </div>
        <div>
            <button type="submit">Login</button>
        </div>
    </form>
</body>
</html>
```
- Página de login simples usando Thymeleaf.

Seguindo esses passos, você terá configurado a autenticação básica com Spring Security no seu projeto Java.

