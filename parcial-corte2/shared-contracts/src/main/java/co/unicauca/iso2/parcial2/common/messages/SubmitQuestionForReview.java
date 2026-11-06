/**
 * @file SubmitQuestionForReview.java
 * @brief Comando asíncrono para enviar una pregunta a revisión.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.common.messages;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @brief Comando SubmitQuestionForReview (Punto 2.2).
 *
 * Publicado por question-service en saberpro.direct.exchange (routing key
 * question.submit) y consumido por lifecycle-service en
 * question.submit.review.queue.
 */
public class SubmitQuestionForReview implements Serializable {
    private String operationId;
    private Long questionId;
    private Long requestedBy;
    private LocalDateTime requestedAt;

    public SubmitQuestionForReview() {}

    public SubmitQuestionForReview(String operationId, Long questionId, Long requestedBy, LocalDateTime requestedAt) {
        this.operationId = operationId;
        this.questionId = questionId;
        this.requestedBy = requestedBy;
        this.requestedAt = requestedAt;
    }

    public String getOperationId() { return operationId; }
    public void setOperationId(String operationId) { this.operationId = operationId; }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public Long getRequestedBy() { return requestedBy; }
    public void setRequestedBy(Long requestedBy) { this.requestedBy = requestedBy; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}
