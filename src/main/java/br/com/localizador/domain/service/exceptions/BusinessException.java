package br.com.localizador.domain.service.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class BusinessException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = -6384443000948644321L;

    public BusinessException(String message) {
        super(message);
    }
}
