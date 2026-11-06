/**
 * @file OperationResult.java
 * @brief Resultado del procesamiento de un operationId.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.domain;

import co.unicauca.iso2.parcial2.common.dto.OperationResultDTO;
import co.unicauca.iso2.parcial2.common.enums.OperationStatus;

import java.time.LocalDateTime;

public class OperationResult {
    private final String operationId;
    private final Long questionId;
    private final OperationStatus status;
    private final String message;
    private final LocalDateTime processedAt;

    private OperationResult(String operationId, Long questionId, OperationStatus status, String message) {
        this.operationId = operationId;
        this.questionId = questionId;
        this.status = status;
        this.message = message;
        this.processedAt = LocalDateTime.now();
    }

    public static OperationResult completed(String operationId, Long questionId, String message) {
        return new OperationResult(operationId, questionId, OperationStatus.COMPLETED, message);
    }

    public static OperationResult failed(String operationId, Long questionId, String message) {
        return new OperationResult(operationId, questionId, OperationStatus.FAILED, message);
    }

    public OperationResultDTO toDTO() {
        return new OperationResultDTO(operationId, questionId, status, message, processedAt);
    }

    public String getOperationId() { return operationId; }
    public Long getQuestionId() { return questionId; }
    public OperationStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
