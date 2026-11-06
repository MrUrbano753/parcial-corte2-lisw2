/**
 * @file LifecycleServiceTest.java
 * @brief Pruebas del consumo idempotente del comando.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.service;

import co.unicauca.iso2.parcial2.common.enums.OperationStatus;
import co.unicauca.iso2.parcial2.common.messages.SubmitQuestionForReview;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.OperationResult;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.QuestionStatus;
import co.unicauca.iso2.parcial2.lifecycleservice.repository.LifecycleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class LifecycleServiceTest {

    private final LifecycleRepository repository = new LifecycleRepository();
    private final LifecycleService service = new LifecycleService(repository);

    private static SubmitQuestionForReview comando(String operationId) {
        return new SubmitQuestionForReview(operationId, 10L, 2L, LocalDateTime.now());
    }

    @Test
    @DisplayName("El primer envío completa la transición a PENDING_REVIEW")
    void primerEnvio() {
        OperationResult r = service.submitForReview(comando("op-1"));
        assertEquals(OperationStatus.COMPLETED, r.getStatus());
        assertEquals(QuestionStatus.PENDING_REVIEW, repository.getOrCreate(10L).currentStatus());
    }

    @Test
    @DisplayName("Un segundo envío con otra operación queda FAILED")
    void segundoEnvio() {
        service.submitForReview(comando("op-1"));
        assertEquals(OperationStatus.FAILED, service.submitForReview(comando("op-2")).getStatus());
    }

    @Test
    @DisplayName("La re-entrega del mismo operationId es idempotente")
    void reentrega() {
        OperationResult primero = service.submitForReview(comando("op-1"));
        assertSame(primero, service.submitForReview(comando("op-1")));
    }
}
