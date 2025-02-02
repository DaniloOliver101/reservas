package br.com.reservas_api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/reserva")
public class ReservaController {
    @PostMapping("/")
    public ResponseEntity<String> novaReserva() {

        return ResponseEntity.status(HttpStatus.CREATED).body("Reserva criada com sucesso");
    }

    public ResponseEntity<List<String>> listaReservas() {

        return ResponseEntity.status((HttpStatus.OK)).body(List.of("Reserva 1", "Reserva 2 "));
    }


    public ResponseEntity<String> atualizarReserva() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Reserva Atualizada com sucesso!");
    }

    public ResponseEntity cancelarReserva() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}