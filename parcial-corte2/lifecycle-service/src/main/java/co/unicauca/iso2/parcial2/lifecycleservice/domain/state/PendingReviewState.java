/**
 * @file PendingReviewState.java
 * @brief Estado Pendiente de Revisión.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.domain.state;

import co.unicauca.iso2.parcial2.lifecycleservice.domain.IllegalStateTransitionException;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.QuestionStatus;

/**
 * Pendiente de revisión: ya fue enviada, reenviarla no está permitido.
 * Los estados siguientes (en revisión, aprobada, rechazada) se agregarían como
 * clases nuevas sin modificar DraftState (principio abierto/cerrado).
 */
public final class PendingReviewState implements IQuestionState {

    @Override
    public void submitForReview(QuestionLifecycle context) {
        throw new IllegalStateTransitionException(context.getQuestionId(), status(), "submitForReview");
    }

    @Override
    public QuestionStatus status() {
        return QuestionStatus.PENDING_REVIEW;
    }
}
