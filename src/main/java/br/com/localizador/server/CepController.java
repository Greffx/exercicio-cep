package br.com.localizador.server;

import br.com.localizador.domain.entity.Cep;
import br.com.localizador.domain.service.CepService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/ceps")
public class CepController {

    private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importarCep(@RequestParam MultipartFile arquivo) {
        var resultado = new Cep();

        try {
            resultado = cepService.extrairCepsDeArquivo(arquivo);
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PostMapping(value = "/viagens", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> calcularMenorCusto(@RequestParam MultipartFile arquivo) {
        try {
            return ResponseEntity.ok().body(cepService.calcularRotaMaisBarata(arquivo));

        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
