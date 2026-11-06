/**
 * @file IQuestionBuilder.java
 * @brief Contrato del patrón Builder (Punto 1.1).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain.builder;

import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;

/**
 * Patrón GoF <b>Builder</b> (creacional) — Punto 1.1.
 * <p>
 * Interfaz fluida: cada paso devuelve el mismo builder para encadenar llamadas.
 * {@link #build()} valida la estructura del reactivo ANTES de instanciar la pregunta.
 */
public interface IQuestionBuilder {

    int REQUIRED_OPTIONS = 4;

    IQuestionBuilder statement(String text);

    IQuestionBuilder justification(String justification);

    IQuestionBuilder author(Long authorId);

    /** Agrega una opción: letra (A-D) y texto. */
    IQuestionBuilder option(String id, String text);

    /** Identifica la respuesta correcta por su letra. */
    IQuestionBuilder correctOption(String optionId);

    /**
     * @return la pregunta en DRAFT
     * @throws co.unicauca.iso2.parcial2.questionservice.domain.QuestionException (400) con TODOS los errores encontrados
     */
    Question build();
}
