/**
 * @file IllegalStateTransitionException.java
 * @brief Transición no permitida por el estado actual.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.domain;

public class IllegalStateTransitionException extends RuntimeException {

    public IllegalStateTransitionException(Long questionId, QuestionStatus current, String action) {
        super("Transición rechazada: la pregunta " + questionId + " está en " + current
                + " y no admite la acción '" + action + "'");
    }
}
