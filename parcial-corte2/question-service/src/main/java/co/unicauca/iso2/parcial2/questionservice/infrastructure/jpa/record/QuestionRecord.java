/**
 * @file QuestionRecord.java
 * @brief Entidad JPA de la tabla questions (Punto 3.2).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.record;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief Registro de persistencia de una pregunta (tabla 'questions').
 *
 * Es un detalle de infraestructura: el dominio nunca lo ve. Es el lado inverso
 * de la relación 1-N (mappedBy = "question"); la FK question_id la tiene
 * AnswerOptionRecord.
 */
@Entity
@Table(name = "questions")
public class QuestionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", nullable = false, length = 1000)
    private String text;

    @Column(name = "justification_text", nullable = false, length = 2000)
    private String justification;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    /**
     * CascadeType.ALL: al guardar/borrar la pregunta se guardan/borran sus opciones.
     * orphanRemoval: si una opción sale de la lista, se elimina de la BD.
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("optionLabel ASC")
    private List<AnswerOptionRecord> options = new ArrayList<>();

    public QuestionRecord() {}

    /** Mantiene sincronizados los dos lados de la relación bidireccional. */
    public void addOption(AnswerOptionRecord option) {
        options.add(option);
        option.setQuestion(this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<AnswerOptionRecord> getOptions() { return options; }
}
