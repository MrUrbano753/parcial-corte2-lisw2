/**
 * @file LifecycleService.java
 * @brief Caso de uso: aplicar la transición a PENDING_REVIEW.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.service;

import co.unicauca.iso2.parcial2.common.messages.SubmitQuestionForReview;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.IllegalStateTransitionException;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.OperationResult;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.QuestionStatus;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.state.QuestionLifecycle;
import co.unicauca.iso2.parcial2.lifecycleservice.repository.LifecycleRepository;
import org.springframework.stereotype.Service;

@Service
public class LifecycleService {

    private final LifecycleRepository repository;

    public LifecycleService(LifecycleRepository repository) {
        this.repository = repository;
    }

    /**
     * Idempotente: si RabbitMQ re-entrega el mismo operationId, se devuelve el resultado
     * ya registrado en lugar de procesarlo otra vez.
     */
    public OperationResult submitForReview(SubmitQuestionForReview command) {
        return repository.findOperationById(command.getOperationId())
                .orElseGet(() -> process(command));
    }

    private OperationResult process(SubmitQuestionForReview command) {
        QuestionLifecycle lifecycle = repository.getOrCreate(command.getQuestionId());
        QuestionStatus from = lifecycle.currentStatus();
        OperationResult result;
        try {
            lifecycle.submitForReview("Autor " + command.getRequestedBy());   // Patrón State, sin if/switch
            result = OperationResult.completed(command.getOperationId(), command.getQuestionId(),
                    "Transición " + from + " -> " + lifecycle.currentStatus() + " completada");
        } catch (IllegalStateTransitionException e) {
            // Error de negocio: reintentar no lo arregla. Se registra FAILED y se confirma el mensaje.
            result = OperationResult.failed(command.getOperationId(), command.getQuestionId(), e.getMessage());
        }
        return repository.saveOperation(result);
    }
}
