/**
 * @file OperationStatus.java
 * @brief Estados de una operación asíncrona.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.common.enums;

/** @brief Resultado de procesar un comando asíncrono identificado por su operationId. */
public enum OperationStatus {
    PENDING,
    COMPLETED,
    FAILED
}
