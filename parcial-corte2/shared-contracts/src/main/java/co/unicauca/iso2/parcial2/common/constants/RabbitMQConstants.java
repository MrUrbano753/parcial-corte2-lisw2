/**
 * @file RabbitMQConstants.java
 * @brief Topología AMQP del parcial (exchange, cola, routing key y DLQ).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.common.constants;

/**
 * @brief Nombres de la topología RabbitMQ exigidos por el enunciado (Punto 2.2).
 *
 * Productor (question-service) y consumidor (lifecycle-service) usan estas
 * constantes, así no hay nombres escritos a mano que se desalineen.
 */
public final class RabbitMQConstants {

    private RabbitMQConstants() {
    }

    /** Exchange directo del parcial. */
    public static final String EXCHANGE_NAME = "saberpro.direct.exchange";

    /** Comando SubmitQuestionForReview (Question Service -> Lifecycle Service). */
    public static final String QUEUE_QUESTION_SUBMIT_REVIEW = "question.submit.review.queue";
    public static final String ROUTING_KEY_QUESTION_SUBMIT = "question.submit";

    /** Mensajes que fallan después de los reintentos. */
    public static final String DEAD_LETTER_EXCHANGE = "saberpro.dlx";
    public static final String DEAD_LETTER_SUFFIX = ".dlq";
}
