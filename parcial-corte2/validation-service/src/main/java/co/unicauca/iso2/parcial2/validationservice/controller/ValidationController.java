/**
 * @file ValidationController.java
 * @brief API REST de validación (POST /validate).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.validationservice.controller;

import co.unicauca.iso2.parcial2.common.dto.CreateQuestionRequest;
import co.unicauca.iso2.parcial2.common.dto.ValidationResult;
import co.unicauca.iso2.parcial2.validationservice.domain.QuestionValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Responde siempre 200: "inválida" es un resultado de negocio, no un error HTTP.
 * Quien decide el 400 hacia el cliente es question-service.
 */
@RestController
@RequestMapping("/validate")
@CrossOrigin(origins = "*")
@Tag(name = "Validación", description = "Validación síncrona del reactivo (Punto 2.1 del parcial)")
public class ValidationController {
    private static final Logger log = LoggerFactory.getLogger(ValidationController.class);

    private final QuestionValidator validator;

    public ValidationController(QuestionValidator validator) {
        this.validator = validator;
    }

    @PostMapping
    @Operation(summary = "Validar pregunta síncronamente",
            description = "La justificación es válida si tiene más de 15 caracteres.")
    public ResponseEntity<ValidationResult> validateQuestion(@RequestBody CreateQuestionRequest request) {
        ValidationResult result = validator.validate(request);
        log.info("[VALIDATION-SERVICE] POST /validate -> valid={} errors={}", result.isValid(), result.getErrors());
        return ResponseEntity.ok(result);
    }
}
