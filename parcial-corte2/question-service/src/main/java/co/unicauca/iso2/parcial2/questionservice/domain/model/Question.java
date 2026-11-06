/**
 * @file Question.java
 * @brief Entidad de negocio Pregunta (Java puro, Punto 3.1).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain.model;

import java.util.List;
import java.util.Objects;

/**
 * @brief Pregunta de selección múltiple con única respuesta.
 *
 * Punto 3.1: clase Java PURA. No tiene @Entity, @Table, @Id ni ninguna
 * dependencia de JPA o Spring. Su persistencia vive en
 * infrastructure.jpa.record.QuestionRecord y se traduce con QuestionDomainMapper.
 *
 * El constructor es privado: las preguntas nuevas se crean con QuestionBuilder
 * (Punto 1.1), que valida antes de instanciar; las ya guardadas se reconstruyen
 * con {@link #restore}.
 */
public final class Question {

    private final Long id;
    private final String text;
    private final String justification;
    private final Long authorId;
    private final QuestionStatus status;
    private final List<AnswerOption> options;

    private Question(Long id, String text, String justification, Long authorId,
                     QuestionStatus status, List<AnswerOption> options) {
        this.id = id;
        this.text = Objects.requireNonNull(text, "text");
        this.justification = Objects.requireNonNull(justification, "justification");
        this.authorId = authorId;
        this.status = Objects.requireNonNull(status, "status");
        this.options = List.copyOf(Objects.requireNonNull(options, "options"));
    }

    /** Pregunta nueva (sin id) en DRAFT. La invoca QuestionBuilder después de validar. */
    public static Question newDraft(String text, String justification, Long authorId, List<AnswerOption> options) {
        return new Question(null, text, justification, authorId, QuestionStatus.DRAFT, options);
    }

    /** Reconstruye una pregunta persistida (la usa el mapeador Record -> Dominio). */
    public static Question restore(Long id, String text, String justification, Long authorId,
                                   QuestionStatus status, List<AnswerOption> options) {
        return new Question(Objects.requireNonNull(id, "id"), text, justification, authorId, status, options);
    }

    /** @return la opción marcada como correcta */
    public AnswerOption correctOption() {
        return options.stream()
                .filter(AnswerOption::isCorrect)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("La pregunta no tiene respuesta correcta"));
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public String getJustification() { return justification; }
    public Long getAuthorId() { return authorId; }
    public QuestionStatus getStatus() { return status; }
    public List<AnswerOption> getOptions() { return options; }
}
