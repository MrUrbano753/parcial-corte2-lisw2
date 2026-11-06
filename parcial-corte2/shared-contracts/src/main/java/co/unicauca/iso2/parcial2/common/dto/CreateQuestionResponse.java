/**
 * @file CreateQuestionResponse.java
 * @brief Respuesta 201 de POST /questions.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.common.dto;

import java.io.Serializable;

public class CreateQuestionResponse implements Serializable {
    private Long questionId;
    private String status;
    private String message;

    public CreateQuestionResponse() {}

    public CreateQuestionResponse(Long questionId, String status, String message) {
        this.questionId = questionId;
        this.status = status;
        this.message = message;
    }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
