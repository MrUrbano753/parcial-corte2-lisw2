/**
 * @file OperationResultDTO.java
 * @brief Resultado consultable de una operación asíncrona.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.common.dto;

import co.unicauca.iso2.parcial2.common.enums.OperationStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OperationResultDTO implements Serializable {
    private String operationId;
    private Long questionId;
    private OperationStatus status;
    private String message;
    private LocalDateTime processedAt;

    public OperationResultDTO() {}

    public OperationResultDTO(String operationId, Long questionId, OperationStatus status,
                              String message, LocalDateTime processedAt) {
        this.operationId = operationId;
        this.questionId = questionId;
        this.status = status;
        this.message = message;
        this.processedAt = processedAt;
    }

    public String getOperationId() { return operationId; }
    public void setOperationId(String operationId) { this.operationId = operationId; }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public OperationStatus getStatus() { return status; }
    public void setStatus(OperationStatus status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
