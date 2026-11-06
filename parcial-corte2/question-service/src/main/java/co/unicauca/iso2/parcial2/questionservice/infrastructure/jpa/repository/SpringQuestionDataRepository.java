/**
 * @file SpringQuestionDataRepository.java
 * @brief Interfaz Spring Data JPA de QuestionRecord.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.repository;

import co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.record.QuestionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** @brief Interfaz tecnológica de Spring Data. No es visible para el dominio. */
@Repository
public interface SpringQuestionDataRepository extends JpaRepository<QuestionRecord, Long> {
}
