/**
 * @file CreateQuestionRequest.java
 * @brief Solicitud REST para crear una pregunta (POST /questions).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @brief Cuerpo de POST /questions y de POST /validate.
 *
 * Pregunta de selección múltiple: enunciado, 4 opciones (A-D), la letra
 * de la respuesta correcta y la justificación que valida validation-service.
 */
public class CreateQuestionRequest implements Serializable {
    private String text;
    private List<OptionDTO> options;
    private String correctOptionId;
    private String justification;
    private Long authorId;

    public CreateQuestionRequest() {}

    public CreateQuestionRequest(String text, List<OptionDTO> options, String correctOptionId,
                                 String justification, Long authorId) {
        this.text = text;
        this.options = options;
        this.correctOptionId = correctOptionId;
        this.justification = justification;
        this.authorId = authorId;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<OptionDTO> getOptions() { return options; }
    public void setOptions(List<OptionDTO> options) { this.options = options; }

    public String getCorrectOptionId() { return correctOptionId; }
    public void setCorrectOptionId(String correctOptionId) { this.correctOptionId = correctOptionId; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
}
