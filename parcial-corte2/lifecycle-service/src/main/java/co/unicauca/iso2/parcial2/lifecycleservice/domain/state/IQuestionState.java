/**
 * @file IQuestionState.java
 * @brief Contrato del patrón State (Punto 1.2).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.domain.state;

import co.unicauca.iso2.parcial2.lifecycleservice.domain.QuestionStatus;

/**
 * Patrón GoF <b>State</b> (comportamiento) — Punto 1.2.
 * <p>
 * Cada estado concreto decide qué hacer ante cada acción. El contexto
 * ({@link QuestionLifecycle}) solo DELEGA, así la transición se resuelve por
 * polimorfismo y no con if/else ni switch sobre un enum.
 */
public interface IQuestionState {

    /** Acción "enviar a revisión". */
    void submitForReview(QuestionLifecycle context);

    /** Estado que representa este objeto. */
    QuestionStatus status();
}
