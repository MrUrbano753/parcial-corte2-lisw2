/**
 * @file QuestionSubmitConsumer.java
 * @brief Consumidor AMQP del comando SubmitQuestionForReview (Punto 2.2).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.consumer;

import co.unicauca.iso2.parcial2.common.constants.RabbitMQConstants;
import co.unicauca.iso2.parcial2.common.messages.SubmitQuestionForReview;
import co.unicauca.iso2.parcial2.lifecycleservice.domain.OperationResult;
import co.unicauca.iso2.parcial2.lifecycleservice.service.LifecycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QuestionSubmitConsumer {
    private static final Logger log = LoggerFactory.getLogger(QuestionSubmitConsumer.class);

    private final LifecycleService lifecycleService;

    public QuestionSubmitConsumer(LifecycleService lifecycleService) {
        this.lifecycleService = lifecycleService;
    }

    /** Consumo asíncrono en question.submit.review.queue y transición a PENDING_REVIEW. */
    @RabbitListener(queues = RabbitMQConstants.QUEUE_QUESTION_SUBMIT_REVIEW)
    public void handleSubmitQuestionForReview(SubmitQuestionForReview command) {
        log.info(">>> [LIFECYCLE-SERVICE] Consumido SubmitQuestionForReview: operationId={}, questionId={}",
                command.getOperationId(), command.getQuestionId());

        OperationResult result = lifecycleService.submitForReview(command);

        log.info("<<< [LIFECYCLE-SERVICE] Operación {} -> {}: {}",
                result.getOperationId(), result.getStatus(), result.getMessage());
    }
}
