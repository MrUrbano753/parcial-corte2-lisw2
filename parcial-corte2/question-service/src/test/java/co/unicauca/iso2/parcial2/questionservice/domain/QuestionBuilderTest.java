/**
 * @file QuestionBuilderTest.java
 * @brief Pruebas del patrón Builder (Punto 1.1).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain;

import co.unicauca.iso2.parcial2.questionservice.domain.builder.QuestionBuilder;
import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;
import co.unicauca.iso2.parcial2.questionservice.domain.model.QuestionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionBuilderTest {

    private static final String JUSTIFICACION = "Builder separa la construcción de la representación.";

    private static QuestionBuilder base() {
        return QuestionBuilder.aQuestion()
                .statement("¿Qué patrón construye objetos complejos paso a paso?")
                .justification(JUSTIFICACION)
                .author(2L);
    }

    @Test
    @DisplayName("Construcción fluida con 4 opciones y 1 correcta queda en DRAFT")
    void construccionValida() {
        Question q = base()
                .option("A", "Singleton").option("B", "Builder").option("C", "Adapter").option("D", "Observer")
                .correctOption("B")
                .build();

        assertNull(q.getId());
        assertEquals(4, q.getOptions().size());
        assertEquals("Builder", q.correctOption().getText());
        assertEquals(QuestionStatus.DRAFT, q.getStatus());
    }

    @Test
    @DisplayName("Con 3 opciones build() falla antes de instanciar")
    void tresOpciones() {
        QuestionException e = assertThrows(QuestionException.class, () -> base()
                .option("A", "Singleton").option("B", "Builder").option("C", "Adapter")
                .correctOption("B").build());
        assertEquals(400, e.httpStatus());
        assertTrue(e.errors().stream().anyMatch(m -> m.contains("exactamente 4 opciones")));
    }

    @Test
    @DisplayName("La respuesta correcta debe coincidir con una opción")
    void correctaInexistente() {
        QuestionException e = assertThrows(QuestionException.class, () -> base()
                .option("A", "Singleton").option("B", "Builder").option("C", "Adapter").option("D", "Observer")
                .correctOption("E").build());
        assertTrue(e.errors().stream().anyMatch(m -> m.contains("'E'")));
    }

    @Test
    @DisplayName("Sin respuesta correcta identificada falla")
    void sinCorrecta() {
        assertThrows(QuestionException.class, () -> base()
                .option("A", "Singleton").option("B", "Builder").option("C", "Adapter").option("D", "Observer")
                .build());
    }

    @Test
    @DisplayName("Letras repetidas hacen ambigua la respuesta correcta")
    void letrasRepetidas() {
        QuestionException e = assertThrows(QuestionException.class, () -> base()
                .option("A", "Singleton").option("A", "Builder").option("C", "Adapter").option("D", "Observer")
                .correctOption("A").build());
        assertTrue(e.errors().stream().anyMatch(m -> m.contains("letras")));
    }

    @Test
    @DisplayName("Un builder vacío acumula todos los errores")
    void acumulaErrores() {
        QuestionException e = assertThrows(QuestionException.class, () -> QuestionBuilder.aQuestion().build());
        assertEquals(4, e.errors().size());
    }
}
