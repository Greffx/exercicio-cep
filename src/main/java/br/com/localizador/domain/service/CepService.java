package br.com.localizador.domain.service;

import br.com.localizador.domain.dto.ViagemDto;
import br.com.localizador.domain.entity.Cep;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CepService {

    Cep extrairCepsDeArquivo(MultipartFile file) throws IOException;

    ViagemDto calcularRotaMaisBarata(MultipartFile file) throws IOException;

    Cep registrarCep(Cep cep);
}
