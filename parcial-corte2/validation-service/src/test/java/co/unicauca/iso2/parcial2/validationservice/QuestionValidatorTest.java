/**
 * @file QuestionValidatorTest.java
 * @brief Pruebas unitarias de la validación de la justificación (sin Spring).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.validationservice;

import co.unicauca.iso2.parcial2.common.dto.CreateQuestionRequest;
import co.unicauca.iso2.parcial2.common.dto.OptionDTO;
import co.unicauca.iso2.parcial2.validationservice.domain.QuestionValidator;
import co.unicauca.iso2.parcial2.validationservice.domain.rules.JustificationRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pruebas del patrón Strategy de validación (sin Spring). */
class QuestionValidatorTest {

    private final QuestionValidator validator = new QuestionValidator(List.of(new JustificationRule()));

    private static CreateQuestionRequest conJustificacion(String justificacion) {
        return new CreateQuestionRequest("¿Qué patrón construye objetos paso a paso?",
                List.of(new OptionDTO("A", "Singleton"), new OptionDTO("B", "Builder"),
                        new OptionDTO("C", "Adapter"), new OptionDTO("D", "Observer")),
                "B", justificacion, 2L);
    }

    @Test
    @DisplayName("Justificación de 16 caracteres es válida")
    void dieciseisCaracteres() {
        assertTrue(validator.validate(conJustificacion("1234567890123456")).isValid());
    }

    @Test
    @DisplayName("Justificación de exactamente 15 caracteres es inválida (debe ser mayor a 15)")
    void quinceCaracteres() {
        assertFalse(validator.validate(conJustificacion("123456789012345")).isValid());
    }

    @Test
    @DisplayName("Justificación nula o en blanco es inválida")
    void nulaOBlanco() {
        assertFalse(validator.validate(conJustificacion(null)).isValid());
        assertFalse(validator.validate(conJustificacion("                    ")).isValid());
    }

    @Test
    @DisplayName("Una solicitud nula se rechaza sin lanzar excepción")
    void solicitudNula() {
        assertFalse(validator.validate(null).isValid());
    }
}
