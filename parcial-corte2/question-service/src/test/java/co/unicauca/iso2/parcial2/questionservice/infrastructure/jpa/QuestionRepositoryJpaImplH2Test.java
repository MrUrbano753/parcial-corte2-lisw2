/**
 * @file QuestionRepositoryJpaImplH2Test.java
 * @brief Verificación de la persistencia JPA en H2 (Punto 3).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa;

import co.unicauca.iso2.parcial2.questionservice.domain.builder.QuestionBuilder;
import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;
import co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.mapper.QuestionDomainMapper;
import co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.record.QuestionRecord;
import co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.repository.QuestionRepositoryJpaImpl;
import co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.repository.SpringQuestionDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({QuestionRepositoryJpaImpl.class, QuestionDomainMapper.class})
class QuestionRepositoryJpaImplH2Test {

    @Autowired
    private QuestionRepositoryJpaImpl repository;

    @Autowired
    private SpringQuestionDataRepository springDataRepo;

    @Autowired
    private QuestionDomainMapper mapper;

    private static Question ejemplo() {
        return QuestionBuilder.aQuestion()
                .statement("¿Qué anotación define una entidad JPA?")
                .justification("@Entity marca la clase como entidad administrada por JPA.")
                .author(2L)
                .option("A", "@Entity").option("B", "@Service").option("C", "@Bean").option("D", "@Component")
                .correctOption("A")
                .build();
    }

    @Test
    @DisplayName("Guarda la pregunta y sus 4 opciones en cascada")
    void guardaEnCascada() {
        Question guardada = repository.save(ejemplo());

        assertNotNull(guardada.getId());
        QuestionRecord record = springDataRepo.findById(guardada.getId()).orElseThrow();
        assertEquals(4, record.getOptions().size());
        assertTrue(record.getOptions().stream().allMatch(o -> o.getId() != null));
        assertEquals("DRAFT", record.getStatus());
    }

    @Test
    @DisplayName("El mapeador es bidireccional: dominio -> record -> dominio")
    void mapeadorBidireccional() {
        Question original = ejemplo();
        Question ida = Question.restore(1L, original.getText(), original.getJustification(),
                original.getAuthorId(), original.getStatus(), original.getOptions());

        Question vuelta = mapper.toDomain(mapper.toRecord(ida));

        assertEquals(ida.getText(), vuelta.getText());
        assertEquals(ida.getOptions(), vuelta.getOptions());
        assertEquals("@Entity", vuelta.correctOption().getText());
    }

    @Test
    @DisplayName("Recupera la pregunta como objeto de dominio")
    void recuperaComoDominio() {
        Question guardada = repository.save(ejemplo());
        Question encontrada = repository.findById(guardada.getId()).orElseThrow();
        assertEquals("A", encontrada.correctOption().getId());
        assertEquals(4, encontrada.getOptions().size());
    }
}
