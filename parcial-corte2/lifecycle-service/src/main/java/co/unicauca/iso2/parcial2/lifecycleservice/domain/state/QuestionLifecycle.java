/**
 * @file QuestionLifecycle.java
 * @brief Contexto del patrón State: ciclo de vida de una pregunta.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.domain.state;

import co.unicauca.iso2.parcial2.common.dto.QuestionStateDTO;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.QuestionStatus;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @brief Contexto del patrón State.
 *
 * submitForReview() no tiene ningún condicional: delega en el estado actual.
 * transitionTo() es package-private: solo las clases State (mismo paquete)
 * pueden cambiar el estado; desde fuera no hay un setter para saltarse las reglas.
 */
public class QuestionLifecycle {

    private final Long questionId;
    private IQuestionState state;
    private LocalDateTime changedAt;
    private String changedBy;
    private String message;

    private QuestionLifecycle(Long questionId) {
        this.questionId = Objects.requireNonNull(questionId, "questionId");
        this.state = new DraftState();
        this.changedAt = LocalDateTime.now();
        this.changedBy = "SYSTEM";
        this.message = "Estado inicial DRAFT";
    }

    /** Toda pregunta nace en borrador. */
    public static QuestionLifecycle startAsDraft(Long questionId) {
        return new QuestionLifecycle(questionId);
    }

    public void submitForReview(String requestedBy) {
        this.changedBy = requestedBy;
        state.submitForReview(this);
    }

    void transitionTo(IQuestionState next, String message) {
        this.state = Objects.requireNonNull(next);
        this.changedAt = LocalDateTime.now();
        this.message = message;
    }

    public QuestionStatus currentStatus() {
        return state.status();
    }

    public Long getQuestionId() {
        return questionId;
    }

    public QuestionStateDTO toDTO() {
        return new QuestionStateDTO(questionId, state.status().name(), changedAt, changedBy, message);
    }
}
