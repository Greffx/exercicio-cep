package br.com.localizador.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table
public class Cep implements Serializable {

    @Serial
    private static final long serialVersionUID = 7990444459855313634L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 100)
    private String cidade;

    /**
     * USANDO 8 pois no arquivo é puro, sem -, caso mude, mudar aqui e na migration
     */
    @Column(name = "cep_inicio", nullable = false, length = 8)
    private String cepInicio;

    @Column(name = "cep_fim", nullable = false, length = 8)
    private String cepFim;

    public Cep() {}

    public Cep(String cidade, String cepInicio, String cepFim) {
        this.cidade = cidade;
        this.cepInicio = cepInicio;
        this.cepFim = cepFim;
    }
}
