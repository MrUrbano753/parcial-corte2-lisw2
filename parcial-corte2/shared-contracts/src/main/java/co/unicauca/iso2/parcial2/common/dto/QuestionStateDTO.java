/**
 * @file QuestionStateDTO.java
 * @brief Estado del ciclo de vida de una pregunta.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.common.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class QuestionStateDTO implements Serializable {
    private Long questionId;
    private String state;
    private LocalDateTime changedAt;
    private String changedBy;
    private String message;

    public QuestionStateDTO() {}

    public QuestionStateDTO(Long questionId, String state, LocalDateTime changedAt, String changedBy, String message) {
        this.questionId = questionId;
        this.state = state;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.message = message;
    }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
