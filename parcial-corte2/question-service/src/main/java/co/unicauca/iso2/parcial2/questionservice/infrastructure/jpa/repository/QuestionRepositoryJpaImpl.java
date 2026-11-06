/**
 * @file QuestionRepositoryJpaImpl.java
 * @brief Implementación JPA del repositorio de dominio (Punto 3.3).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.repository;

import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;
import co.unicauca.iso2.parcial2.questionservice.domain.repository.QuestionRepository;
import co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.mapper.QuestionDomainMapper;
import co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.record.QuestionRecord;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @brief Adapta el contrato del dominio (QuestionRepository) a Spring Data JPA.
 *
 * Inyecta SpringQuestionDataRepository y traduce con QuestionDomainMapper. Las
 * transacciones viven aquí para que la carga LAZY de las opciones ocurra con la
 * sesión abierta (open-in-view está desactivado).
 */
@Repository
public class QuestionRepositoryJpaImpl implements QuestionRepository {

    private final SpringQuestionDataRepository springDataRepo;
    private final QuestionDomainMapper mapper;

    public QuestionRepositoryJpaImpl(SpringQuestionDataRepository springDataRepo, QuestionDomainMapper mapper) {
        this.springDataRepo = springDataRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Question save(Question question) {
        QuestionRecord saved = springDataRepo.save(mapper.toRecord(question));
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Question> findById(Long id) {
        return springDataRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return springDataRepo.findAll().stream().map(mapper::toDomain).toList();
    }
}
