/**
 * @file RabbitReviewRequestPublisher.java
 * @brief Publicador AMQP del comando SubmitQuestionForReview (Punto 2.2).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.client;

import co.unicauca.iso2.parcial2.common.constants.RabbitMQConstants;
import co.unicauca.iso2.parcial2.common.messages.SubmitQuestionForReview;
import co.unicauca.iso2.parcial2.questionservice.domain.QuestionException;
import co.unicauca.iso2.parcial2.questionservice.domain.port.ReviewRequestPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/** Adaptador de mensajería: publica {@link SubmitQuestionForReview} en {@code saberpro.direct.exchange}. */
@Component
public class RabbitReviewRequestPublisher implements ReviewRequestPublisher {
    private static final Logger log = LoggerFactory.getLogger(RabbitReviewRequestPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitReviewRequestPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(SubmitQuestionForReview command) {
        log.info("Publicando SubmitQuestionForReview [exchange: {}, routingKey: {}] questionId={}, operationId={}",
                RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.ROUTING_KEY_QUESTION_SUBMIT,
                command.getQuestionId(), command.getOperationId());
        try {
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME,
                    RabbitMQConstants.ROUTING_KEY_QUESTION_SUBMIT, command, message -> {
                        // messageId = operationId: trazabilidad e idempotencia en el consumidor
                        message.getMessageProperties().setMessageId(command.getOperationId());
                        return message;
                    });
        } catch (AmqpException e) {
            log.error("RabbitMQ no disponible al enviar la pregunta {} a revisión: {}", command.getQuestionId(), e.getMessage());
            throw QuestionException.unavailable("El bus de mensajes no está disponible; intente de nuevo.");
        }
    }
}
