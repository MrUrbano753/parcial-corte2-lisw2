/**
 * @file QuestionStatus.java
 * @brief Estados de la pregunta en el contexto de Autoría.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain.model;

public enum QuestionStatus {
    DRAFT("Borrador"),
    PENDING_REVIEW("Pendiente de revisión");

    private final String label;

    QuestionStatus(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
