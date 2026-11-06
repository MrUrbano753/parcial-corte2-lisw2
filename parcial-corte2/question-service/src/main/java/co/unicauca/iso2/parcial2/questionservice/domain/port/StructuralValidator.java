/**
 * @file StructuralValidator.java
 * @brief Puerto hacia la validación externa (validation-service).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain.port;

import co.unicauca.iso2.parcial2.common.dto.CreateQuestionRequest;
import co.unicauca.iso2.parcial2.common.dto.ValidationResult;

/** Lo implementa ValidationClient con RestTemplate (Punto 2.1). */
public interface StructuralValidator {

    ValidationResult validate(CreateQuestionRequest request);
}
