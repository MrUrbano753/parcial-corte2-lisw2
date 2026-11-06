/**
 * @file DraftState.java
 * @brief Estado Borrador.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.domain.state;

import co.unicauca.iso2.parcial2.lifecycleservice.domain.QuestionStatus;

/** Borrador: enviar a revisión lo lleva a Pendiente de Revisión. */
public final class DraftState implements IQuestionState {

    @Override
    public void submitForReview(QuestionLifecycle context) {
        context.transitionTo(new PendingReviewState(), "Pregunta enviada a revisión mediante RabbitMQ");
    }

    @Override
    public QuestionStatus status() {
        return QuestionStatus.DRAFT;
    }
}
