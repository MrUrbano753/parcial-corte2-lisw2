/**
 * @file QuestionAuthoringFacadeTest.java
 * @brief Pruebas de los casos de uso con dobles de prueba (sin Spring).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.application;

import co.unicauca.iso2.parcial2.common.dto.CreateQuestionRequest;
import co.unicauca.iso2.parcial2.common.dto.OptionDTO;
import co.unicauca.iso2.parcial2.common.dto.ValidationResult;
import co.unicauca.iso2.parcial2.common.messages.SubmitQuestionForReview;
import co.unicauca.iso2.parcial2.questionservice.domain.QuestionException;
import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;
import co.unicauca.iso2.parcial2.questionservice.domain.model.QuestionStatus;
import co.unicauca.iso2.parcial2.questionservice.domain.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionAuthoringFacadeTest {

    /** Repositorio en memoria que simula el id generado por la BD. */
    static class RepoEnMemoria implements QuestionRepository {
        final Map<Long, Question> datos = new HashMap<>();
        final AtomicLong secuencia = new AtomicLong();

        public Question save(Question q) {
            Question guardada = Question.restore(secuencia.incrementAndGet(), q.getText(), q.getJustification(),
                    q.getAuthorId(), q.getStatus(), q.getOptions());
            datos.put(guardada.getId(), guardada);
            return guardada;
        }

        public Optional<Question> findById(Long id) { return Optional.ofNullable(datos.get(id)); }

        public List<Question> findAll() { return List.copyOf(datos.values()); }
    }

    private final RepoEnMemoria repo = new RepoEnMemoria();
    private final List<SubmitQuestionForReview> publicados = new ArrayList<>();

    private static CreateQuestionRequest solicitud(String justificacion) {
        return new CreateQuestionRequest("¿Qué patrón evita condicionales al cambiar de estado?",
                List.of(new OptionDTO("A", "State"), new OptionDTO("B", "Builder"),
                        new OptionDTO("C", "Facade"), new OptionDTO("D", "Proxy")),
                "A", justificacion, 2L);
    }

    @Test
    @DisplayName("Si validation-service aprueba, se guarda en DRAFT")
    void guardaEnDraft() {
        var facade = new QuestionAuthoringFacade(repo, r -> ValidationResult.ok(), publicados::add);

        Question q = facade.create(solicitud("State delega el comportamiento al estado actual."));

        assertNotNull(q.getId());
        assertEquals(QuestionStatus.DRAFT, q.getStatus());
        assertEquals(1, repo.datos.size());
    }

    @Test
    @DisplayName("Si validation-service rechaza, responde 400 y no guarda")
    void rechazadaNoSeGuarda() {
        var facade = new QuestionAuthoringFacade(repo,
                r -> ValidationResult.fail(List.of("La justificación debe tener más de 15 caracteres")),
                publicados::add);

        QuestionException e = assertThrows(QuestionException.class, () -> facade.create(solicitud("Corta")));
        assertEquals(400, e.httpStatus());
        assertTrue(repo.datos.isEmpty());
    }

    @Test
    @DisplayName("Si la estructura es inválida no se llama al servicio remoto")
    void estructuraInvalida() {
        var facade = new QuestionAuthoringFacade(repo, r -> {
            throw new AssertionError("No debía invocarse validation-service");
        }, publicados::add);

        CreateQuestionRequest mala = solicitud("Justificación suficientemente larga");
        mala.setOptions(mala.getOptions().subList(0, 3));
        assertThrows(QuestionException.class, () -> facade.create(mala));
        assertTrue(repo.datos.isEmpty());
    }

    @Test
    @DisplayName("Enviar a revisión publica el comando con un operationId")
    void enviarPublica() {
        var facade = new QuestionAuthoringFacade(repo, r -> ValidationResult.ok(), publicados::add);
        Question q = facade.create(solicitud("State delega el comportamiento al estado actual."));

        String operationId = facade.submitForReview(q.getId(), null);

        assertEquals(1, publicados.size());
        assertEquals(operationId, publicados.get(0).getOperationId());
        assertEquals(q.getId(), publicados.get(0).getQuestionId());
    }

    @Test
    @DisplayName("Enviar una pregunta inexistente responde 404 y no publica")
    void enviarInexistente() {
        var facade = new QuestionAuthoringFacade(repo, r -> ValidationResult.ok(), publicados::add);
        QuestionException e = assertThrows(QuestionException.class, () -> facade.submitForReview(999L, null));
        assertEquals(404, e.httpStatus());
        assertTrue(publicados.isEmpty());
    }
}
