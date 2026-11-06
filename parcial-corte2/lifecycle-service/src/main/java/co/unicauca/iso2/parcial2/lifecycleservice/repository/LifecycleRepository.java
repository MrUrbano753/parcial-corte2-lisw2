/**
 * @file LifecycleRepository.java
 * @brief Almacén en memoria de estados y operaciones.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.repository;

import co.unicauca.iso2.parcial2.lifecycleservice.domain.OperationResult;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.state.QuestionLifecycle;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LifecycleRepository {
    private final Map<Long, QuestionLifecycle> lifecyclesByQuestionId = new ConcurrentHashMap<>();
    private final Map<String, OperationResult> operationsById = new ConcurrentHashMap<>();

    /** Una pregunta sin historial está en DRAFT (así nace en question-service). */
    public QuestionLifecycle getOrCreate(Long questionId) {
        return lifecyclesByQuestionId.computeIfAbsent(questionId, QuestionLifecycle::startAsDraft);
    }

    public Optional<QuestionLifecycle> findByQuestionId(Long questionId) {
        return Optional.ofNullable(lifecyclesByQuestionId.get(questionId));
    }

    public OperationResult saveOperation(OperationResult operation) {
        operationsById.put(operation.getOperationId(), operation);
        return operation;
    }

    public Optional<OperationResult> findOperationById(String operationId) {
        return Optional.ofNullable(operationsById.get(operationId));
    }
}
