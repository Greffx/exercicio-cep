package br.com.localizador.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ViagemDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -1246622575246302233L;

    private List<String> rota;
    Double menorDistancia;

    public ViagemDto(List<String> rota, Double menorDistancia) {
        this.rota = rota;
        this.menorDistancia = menorDistancia;
    }
}
