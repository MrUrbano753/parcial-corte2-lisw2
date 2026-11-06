/**
 * @file JustificationRule.java
 * @brief Regla del parcial: la justificación debe tener más de 15 caracteres.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.validationservice.domain.rules;

import co.unicauca.iso2.parcial2.common.dto.CreateQuestionRequest;
import co.unicauca.iso2.parcial2.validationservice.domain.ValidationRule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Punto 2.1: la justificación del reactivo es válida si su longitud es
 * <b>mayor</b> a 15 caracteres (15 exactos es inválida).
 */
@Component
@Order(1)
public class JustificationRule implements ValidationRule {

    static final int MIN_LENGTH_EXCLUSIVE = 15;

    @Override
    public String name() {
        return "Justificación";
    }

    @Override
    public List<String> check(CreateQuestionRequest q) {
        String justification = q.getJustification() == null ? "" : q.getJustification().trim();
        if (justification.length() > MIN_LENGTH_EXCLUSIVE) {
            return List.of();
        }
        return List.of("La justificación debe tener más de " + MIN_LENGTH_EXCLUSIVE
                + " caracteres (actual: " + justification.length() + ").");
    }
}
