/**
 * @file LifecycleController.java
 * @brief Consulta del estado y del resultado de las operaciones.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.controller;

import co.unicauca.iso2.parcial2.common.dto.OperationResultDTO;
import co.unicauca.iso2.parcial2.common.dto.QuestionStateDTO;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.OperationResult;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.state.QuestionLifecycle;
import co.unicauca.iso2.parcial2.lifecycleservice.repository.LifecycleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Ciclo de Vida", description = "Estado de las preguntas y resultado de las operaciones asíncronas")
public class LifecycleController {

    private final LifecycleRepository repository;

    public LifecycleController(LifecycleRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/lifecycle/{questionId}")
    @Operation(summary = "Estado actual de una pregunta (DRAFT o PENDING_REVIEW)")
    public ResponseEntity<QuestionStateDTO> getQuestionState(@PathVariable Long questionId) {
        return ResponseEntity.ok(repository.getOrCreate(questionId).toDTO());
    }

    @GetMapping("/lifecycle/operations/{operationId}")
    @Operation(summary = "Resultado de una operación asíncrona (COMPLETED o FAILED)")
    public ResponseEntity<?> getOperation(@PathVariable String operationId) {
        return repository.findOperationById(operationId)
                .map(OperationResult::toDTO)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Operación aún no procesada o inexistente", "operationId", operationId)));
    }
}
