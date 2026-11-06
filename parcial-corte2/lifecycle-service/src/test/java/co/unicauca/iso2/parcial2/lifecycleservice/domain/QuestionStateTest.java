/**
 * @file QuestionStateTest.java
 * @brief Pruebas del patrón State (Punto 1.2).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.domain;

import co.unicauca.iso2.parcial2.lifecycleservice.domain.state.QuestionLifecycle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionStateTest {

    @Test
    @DisplayName("Toda pregunta nace en DRAFT")
    void naceEnDraft() {
        assertEquals(QuestionStatus.DRAFT, QuestionLifecycle.startAsDraft(1L).currentStatus());
    }

    @Test
    @DisplayName("DraftState -> PendingReviewState sin condicionales")
    void draftAPendiente() {
        QuestionLifecycle lc = QuestionLifecycle.startAsDraft(1L);
        lc.submitForReview("Autor 2");
        assertEquals(QuestionStatus.PENDING_REVIEW, lc.currentStatus());
    }

    @Test
    @DisplayName("PendingReviewState rechaza un segundo envío")
    void pendienteNoReenvia() {
        QuestionLifecycle lc = QuestionLifecycle.startAsDraft(1L);
        lc.submitForReview("Autor 2");
        assertThrows(IllegalStateTransitionException.class, () -> lc.submitForReview("Autor 2"));
        assertEquals(QuestionStatus.PENDING_REVIEW, lc.currentStatus());
    }
}
