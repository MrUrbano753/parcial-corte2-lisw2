/**
 * @file QuestionController.java
 * @brief API REST de preguntas (POST /questions y POST /questions/{id}/submit).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.controller;

import co.unicauca.iso2.parcial2.common.dto.CreateQuestionRequest;
import co.unicauca.iso2.parcial2.common.dto.CreateQuestionResponse;
import co.unicauca.iso2.parcial2.common.dto.QuestionDTO;
import co.unicauca.iso2.parcial2.questionservice.application.QuestionAuthoringFacade;
import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

/** Solo traduce HTTP; la lógica está en {@link QuestionAuthoringFacade}. */
@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "*")
@Tag(name = "Preguntas", description = "Creación y envío a revisión de preguntas (Puntos 2.1 y 2.2)")
public class QuestionController {

    private final QuestionAuthoringFacade facade;

    public QuestionController(QuestionAuthoringFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    @Operation(summary = "Crear pregunta (Punto 2.1)",
            description = "Valida síncronamente con validation-service. Si es válida la guarda en DRAFT (201); si no, responde 400 sin guardar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pregunta validada y creada en DRAFT",
                    content = @Content(schema = @Schema(implementation = CreateQuestionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Estructura inválida o justificación de 15 caracteres o menos", content = @Content),
            @ApiResponse(responseCode = "503", description = "validation-service no responde", content = @Content)
    })
    public ResponseEntity<CreateQuestionResponse> createQuestion(@RequestBody CreateQuestionRequest request) {
        Question saved = facade.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(new CreateQuestionResponse(
                saved.getId(), saved.getStatus().name(), "Pregunta validada y persistida exitosamente en estado DRAFT"));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Enviar pregunta a revisión de forma asíncrona (Punto 2.2)",
            description = "Publica SubmitQuestionForReview en saberpro.direct.exchange y responde 202 con un operationId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Solicitud aceptada para procesamiento asíncrono"),
            @ApiResponse(responseCode = "404", description = "La pregunta no existe", content = @Content),
            @ApiResponse(responseCode = "503", description = "RabbitMQ no disponible", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> submitForReview(@PathVariable Long id,
                                                               @RequestParam(required = false) Long requestedBy) {
        String operationId = facade.submitForReview(id, requestedBy);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "operationId", operationId,
                "questionId", id,
                "status", "PENDING",
                "message", "Solicitud de envío a revisión aceptada para procesamiento asíncrono"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener el detalle de una pregunta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pregunta encontrada",
                    content = @Content(schema = @Schema(implementation = QuestionDTO.class))),
            @ApiResponse(responseCode = "404", description = "La pregunta no existe", content = @Content)
    })
    public ResponseEntity<QuestionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facade.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar preguntas guardadas en H2")
    public ResponseEntity<List<QuestionDTO>> findAll() {
        return ResponseEntity.ok(facade.findAll());
    }
}
