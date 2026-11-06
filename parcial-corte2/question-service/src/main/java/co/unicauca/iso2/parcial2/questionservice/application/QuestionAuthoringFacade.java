/**
 * @file QuestionAuthoringFacade.java
 * @brief Fachada de los casos de uso de Autoría.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.application;

import co.unicauca.iso2.parcial2.common.dto.CreateQuestionRequest;
import co.unicauca.iso2.parcial2.common.dto.OptionDTO;
import co.unicauca.iso2.parcial2.common.dto.QuestionDTO;
import co.unicauca.iso2.parcial2.common.dto.ValidationResult;
import co.unicauca.iso2.parcial2.common.messages.SubmitQuestionForReview;
import co.unicauca.iso2.parcial2.questionservice.domain.QuestionException;
import co.unicauca.iso2.parcial2.questionservice.domain.builder.QuestionBuilder;
import co.unicauca.iso2.parcial2.questionservice.domain.model.AnswerOption;
import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;
import co.unicauca.iso2.parcial2.questionservice.domain.port.ReviewRequestPublisher;
import co.unicauca.iso2.parcial2.questionservice.domain.port.StructuralValidator;
import co.unicauca.iso2.parcial2.questionservice.domain.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Patrón GoF <b>Facade</b> (estructural).
 * <p>
 * Crear o enviar a revisión una pregunta exige coordinar el Builder, validation-service
 * (REST), el repositorio JPA y RabbitMQ. El controlador solo habla con esta fachada,
 * que depende únicamente de interfaces (puertos), no de RestTemplate, RabbitTemplate ni JPA.
 */
@Service
public class QuestionAuthoringFacade {
    private static final Logger log = LoggerFactory.getLogger(QuestionAuthoringFacade.class);
    static final long DEFAULT_AUTHOR_ID = 2L;

    private final QuestionRepository repository;
    private final StructuralValidator validator;
    private final ReviewRequestPublisher publisher;

    public QuestionAuthoringFacade(QuestionRepository repository, StructuralValidator validator,
                                   ReviewRequestPublisher publisher) {
        this.repository = repository;
        this.validator = validator;
        this.publisher = publisher;
    }

    /**
     * Punto 2.1:
     * 1) el Builder valida la estructura (4 opciones, 1 correcta) -> 400 si falla;
     * 2) llamada SÍNCRONA a validation-service -> 400 si la justificación no pasa;
     * 3) solo entonces se guarda en DRAFT -> 201. Si algo falla, NO se guarda nada.
     */
    public Question create(CreateQuestionRequest request) {
        if (request == null) {
            throw QuestionException.badRequest("El cuerpo de la solicitud es obligatorio");
        }
        if (request.getAuthorId() == null) {
            request.setAuthorId(DEFAULT_AUTHOR_ID);
        }
        Question draft = QuestionBuilder.from(request).build();

        ValidationResult result = validator.validate(request);
        if (!result.isValid()) {
            log.warn("Validación fallida, la pregunta no se guarda: {}", result.getErrors());
            throw QuestionException.invalid(result.getErrors());
        }

        Question saved = repository.save(draft);
        log.info("Pregunta {} creada en DRAFT por el autor {}", saved.getId(), saved.getAuthorId());
        return saved;
    }

    /**
     * Punto 2.2: publica el comando y retorna de inmediato el operationId. La transición
     * la hace lifecycle-service después; por eso el controlador responde 202 Accepted.
     */
    public String submitForReview(Long id, Long requestedBy) {
        Question question = repository.findById(id).orElseThrow(() -> QuestionException.notFound(id));
        Long requester = requestedBy != null ? requestedBy : question.getAuthorId();
        String operationId = UUID.randomUUID().toString();
        publisher.publish(new SubmitQuestionForReview(operationId, id, requester, LocalDateTime.now()));
        return operationId;
    }

    public QuestionDTO findById(Long id) {
        return toDTO(repository.findById(id).orElseThrow(() -> QuestionException.notFound(id)));
    }

    public List<QuestionDTO> findAll() {
        return repository.findAll().stream().map(QuestionAuthoringFacade::toDTO).toList();
    }

    /** La conversión a DTO vive aquí para que el dominio no dependa de los contratos REST. */
    static QuestionDTO toDTO(Question q) {
        List<OptionDTO> options = q.getOptions().stream()
                .map(o -> new OptionDTO(o.getId(), o.getText()))
                .toList();
        return new QuestionDTO(q.getId(), q.getText(), options, q.correctOption().getId(),
                q.getJustification(), q.getAuthorId(), q.getStatus().name());
    }
}
