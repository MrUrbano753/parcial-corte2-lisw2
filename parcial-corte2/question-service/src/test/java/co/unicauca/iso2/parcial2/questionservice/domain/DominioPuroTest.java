/**
 * @file DominioPuroTest.java
 * @brief Evidencia del Punto 3.1: Question no tiene anotaciones de persistencia.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain;

import co.unicauca.iso2.parcial2.questionservice.domain.model.AnswerOption;
import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DominioPuroTest {

    private static boolean sinJpa(Class<?> type) {
        Stream<Annotation> anotaciones = Stream.concat(
                Arrays.stream(type.getAnnotations()),
                Arrays.stream(type.getDeclaredFields()).flatMap(f -> Arrays.stream(f.getAnnotations())));
        return anotaciones.noneMatch(a -> a.annotationType().getPackageName().startsWith("jakarta.persistence"));
    }

    @Test
    @DisplayName("Question y AnswerOption no tienen @Entity, @Table ni @Id")
    void dominioSinAnotacionesJpa() {
        assertTrue(sinJpa(Question.class));
        assertTrue(sinJpa(AnswerOption.class));
    }
}
