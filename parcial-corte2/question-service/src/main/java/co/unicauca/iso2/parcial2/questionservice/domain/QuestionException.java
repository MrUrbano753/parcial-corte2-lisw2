/**
 * @file QuestionException.java
 * @brief Error de negocio con el código HTTP que le corresponde.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain;

import java.util.List;

/** Error de negocio del contexto de Autoría con el código HTTP que le corresponde. */
public class QuestionException extends RuntimeException {

    private final int httpStatus;
    private final List<String> errors;

    private QuestionException(int httpStatus, String message, List<String> errors) {
        super(message);
        this.httpStatus = httpStatus;
        this.errors = errors == null ? List.of() : List.copyOf(errors);
    }

    public static QuestionException badRequest(String message) {
        return new QuestionException(400, message, List.of());
    }

    /** Falla del Builder (estructura) o de validation-service (justificación). */
    public static QuestionException invalid(List<String> errors) {
        return new QuestionException(400, "Error de validación de la pregunta", errors);
    }

    public static QuestionException notFound(Long id) {
        return new QuestionException(404, "La pregunta con ID " + id + " no existe", List.of());
    }

    public static QuestionException unavailable(String message) {
        return new QuestionException(503, message, List.of());
    }

    public int httpStatus() {
        return httpStatus;
    }

    public List<String> errors() {
        return errors;
    }
}
