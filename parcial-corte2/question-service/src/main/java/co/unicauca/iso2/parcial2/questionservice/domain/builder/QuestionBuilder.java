/**
 * @file QuestionBuilder.java
 * @brief Builder concreto de preguntas de selección múltiple (Punto 1.1).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain.builder;

import co.unicauca.iso2.parcial2.common.dto.CreateQuestionRequest;
import co.unicauca.iso2.parcial2.common.dto.OptionDTO;
import co.unicauca.iso2.parcial2.questionservice.domain.QuestionException;
import co.unicauca.iso2.parcial2.questionservice.domain.model.AnswerOption;
import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Patrón GoF <b>Builder</b> (creacional).
 * <p>
 * Arma la pregunta paso a paso:
 * <pre>
 * Question q = QuestionBuilder.aQuestion()
 *         .statement("¿Qué patrón construye objetos complejos paso a paso?")
 *         .justification("Builder separa la construcción de la representación.")
 *         .option("A", "Singleton").option("B", "Builder")
 *         .option("C", "Adapter").option("D", "Observer")
 *         .correctOption("B")
 *         .build();
 * </pre>
 * Un builder tiene estado, por eso se crea uno por pregunta y no es un bean de Spring.
 */
public final class QuestionBuilder implements IQuestionBuilder {

    private String text;
    private String justification;
    private Long authorId;
    private String correctOptionId;
    private final List<String[]> options = new ArrayList<>();

    private QuestionBuilder() {
    }

    public static QuestionBuilder aQuestion() {
        return new QuestionBuilder();
    }

    /** Copia los campos de la solicitud REST. */
    public static QuestionBuilder from(CreateQuestionRequest request) {
        QuestionBuilder builder = aQuestion();
        builder.statement(request.getText())
                .justification(request.getJustification())
                .author(request.getAuthorId())
                .correctOption(request.getCorrectOptionId());
        List<OptionDTO> opts = request.getOptions() == null ? List.of() : request.getOptions();
        opts.forEach(o -> builder.option(o.getId(), o.getText()));
        return builder;
    }

    @Override
    public QuestionBuilder statement(String text) {
        this.text = trim(text);
        return this;
    }

    @Override
    public QuestionBuilder justification(String justification) {
        this.justification = trim(justification);
        return this;
    }

    @Override
    public QuestionBuilder author(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    @Override
    public QuestionBuilder option(String id, String text) {
        this.options.add(new String[]{trim(id), trim(text)});
        return this;
    }

    @Override
    public QuestionBuilder correctOption(String optionId) {
        this.correctOptionId = trim(optionId);
        return this;
    }

    @Override
    public Question build() {
        List<String> errors = validateStructure();
        if (!errors.isEmpty()) {
            throw QuestionException.invalid(errors);
        }
        List<AnswerOption> answerOptions = options.stream()
                .map(o -> new AnswerOption(o[0].toUpperCase(Locale.ROOT), o[1], o[0].equalsIgnoreCase(correctOptionId)))
                .toList();
        return Question.newDraft(text, justification, authorId, answerOptions);
    }

    /** Acumula todos los errores para que el autor los corrija de una sola vez. */
    private List<String> validateStructure() {
        List<String> errors = new ArrayList<>();

        if (isBlank(text)) {
            errors.add("El enunciado de la pregunta es obligatorio.");
        }
        if (isBlank(justification)) {
            errors.add("La justificación es obligatoria.");
        }
        if (options.size() != REQUIRED_OPTIONS) {
            errors.add("La pregunta debe tener exactamente " + REQUIRED_OPTIONS
                    + " opciones de respuesta (recibidas: " + options.size() + ").");
        }
        if (options.stream().anyMatch(o -> isBlank(o[0]) || isBlank(o[1]))) {
            errors.add("Cada opción debe tener letra y texto.");
        }
        if (distinct(0) < options.size()) {
            errors.add("Las letras de las opciones no pueden repetirse.");
        }
        if (distinct(1) < options.size()) {
            errors.add("Los textos de las opciones no pueden repetirse.");
        }

        long correctMatches = options.stream()
                .filter(o -> o[0] != null && o[0].equalsIgnoreCase(correctOptionId))
                .count();
        if (isBlank(correctOptionId)) {
            errors.add("Debe identificarse la respuesta correcta.");
        } else if (correctMatches != 1) {
            errors.add("La respuesta correcta '" + correctOptionId
                    + "' debe coincidir con exactamente una de las opciones.");
        }
        return errors;
    }

    private long distinct(int index) {
        return options.stream()
                .map(o -> o[index])
                .filter(Objects::nonNull)
                .map(v -> v.toLowerCase(Locale.ROOT))
                .distinct()
                .count();
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}
