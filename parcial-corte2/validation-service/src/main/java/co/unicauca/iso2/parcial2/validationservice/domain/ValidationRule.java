/**
 * @file ValidationRule.java
 * @brief Estrategia de validación (patrón Strategy).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.validationservice.domain;

import co.unicauca.iso2.parcial2.common.dto.CreateQuestionRequest;

import java.util.List;

/**
 * Patrón GoF <b>Strategy</b> (comportamiento).
 * <p>
 * Cada regla es una estrategia intercambiable. {@link QuestionValidator} no conoce las
 * reglas concretas: Spring le inyecta todas y él las ejecuta en orden. Agregar una regla
 * nueva es crear una clase, sin tocar el validador (principio abierto/cerrado).
 */
public interface ValidationRule {

    /** Nombre corto de la regla, útil para trazas. */
    String name();

    /** Devuelve los errores encontrados; lista vacía si la pregunta cumple la regla. */
    List<String> check(CreateQuestionRequest question);

    static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
