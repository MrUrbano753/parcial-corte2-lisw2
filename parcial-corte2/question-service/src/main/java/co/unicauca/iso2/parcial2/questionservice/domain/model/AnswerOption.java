/**
 * @file AnswerOption.java
 * @brief Opción de respuesta del dominio (Java puro).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain.model;

import java.util.Objects;

/** @brief Opción de respuesta (A, B, C o D). Sin anotaciones de persistencia. */
public class AnswerOption {

    private final String id;
    private final String text;
    private final boolean correct;

    public AnswerOption(String id, String text, boolean correct) {
        this.id = id;
        this.text = text;
        this.correct = correct;
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public boolean isCorrect() { return correct; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerOption other)) return false;
        return correct == other.correct && Objects.equals(id, other.id) && Objects.equals(text, other.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, correct);
    }
}
