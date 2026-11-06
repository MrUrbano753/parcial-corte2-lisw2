/**
 * @file QuestionDTO.java
 * @brief Vista REST de una pregunta.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.common.dto;

import java.io.Serializable;
import java.util.List;

public class QuestionDTO implements Serializable {
    private Long id;
    private String text;
    private List<OptionDTO> options;
    private String correctOptionId;
    private String justification;
    private Long authorId;
    private String status;

    public QuestionDTO() {}

    public QuestionDTO(Long id, String text, List<OptionDTO> options, String correctOptionId,
                       String justification, Long authorId, String status) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctOptionId = correctOptionId;
        this.justification = justification;
        this.authorId = authorId;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
