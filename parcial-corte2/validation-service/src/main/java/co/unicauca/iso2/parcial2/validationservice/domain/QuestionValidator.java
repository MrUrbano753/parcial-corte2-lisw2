/**
 * @file QuestionValidator.java
 * @brief Contexto del patrón Strategy: aplica todas las reglas.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.validationservice.domain;

import co.unicauca.iso2.parcial2.common.dto.CreateQuestionRequest;
import co.unicauca.iso2.parcial2.common.dto.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.List;

/** Ejecuta todas las reglas y acumula los errores (no se detiene en el primero). */
@Service
public class QuestionValidator {

    private final List<ValidationRule> rules;

    public QuestionValidator(List<ValidationRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public ValidationResult validate(CreateQuestionRequest request) {
        if (request == null) {
            return ValidationResult.fail(List.of("La solicitud de validación es obligatoria."));
        }
        List<String> errors = rules.stream()
                .flatMap(rule -> rule.check(request).stream())
                .toList();
        return errors.isEmpty() ? ValidationResult.ok() : ValidationResult.fail(errors);
    }
}
