/**
 * @file QuestionRepository.java
 * @brief Puerto de persistencia del dominio.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain.repository;

import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;

import java.util.List;
import java.util.Optional;

/**
 * Contrato del dominio para guardar preguntas. El dominio no sabe si detrás hay
 * JPA, JDBC o memoria: la implementación es QuestionRepositoryJpaImpl (DIP).
 */
public interface QuestionRepository {

    Question save(Question question);

    Optional<Question> findById(Long id);

    List<Question> findAll();
}
