/**
 * @file QuestionStatus.java
 * @brief Estados visibles del ciclo de vida.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.domain;

/**
 * @brief Nombre de cada estado (para consultas y logs).
 *
 * El COMPORTAMIENTO de cada estado no está aquí sino en las clases State
 * (DraftState, PendingReviewState).
 */
public enum QuestionStatus {
    DRAFT,
    PENDING_REVIEW
}
