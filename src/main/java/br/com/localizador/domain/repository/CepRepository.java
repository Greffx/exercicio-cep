package br.com.localizador.domain.repository;

import br.com.localizador.domain.entity.Cep;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CepRepository extends SimpleJpaRepository<Cep, UUID> {

    @PersistenceContext
    private EntityManager em;

    public CepRepository(EntityManager em) {
        super(Cep.class, em);
    }

    public Cep registrarCep(Cep cep) {
        return em.merge(cep);
    }
}
