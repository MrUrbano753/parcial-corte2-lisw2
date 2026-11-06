/**
 * @file RabbitMQConfig.java
 * @brief Topología RabbitMQ del lado consumidor (con DLQ).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.lifecycleservice.config;

import co.unicauca.iso2.parcial2.common.constants.RabbitMQConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * saberpro.direct.exchange --(question.submit)--> question.submit.review.queue --> @RabbitListener
 * Si un mensaje falla tras los reintentos: saberpro.dlx --> question.submit.review.queue.dlq
 */
@Configuration
public class RabbitMQConfig {

    private static final String DLQ = RabbitMQConstants.QUEUE_QUESTION_SUBMIT_REVIEW + RabbitMQConstants.DEAD_LETTER_SUFFIX;

    @Bean
    public DirectExchange saberproExchange() {
        return new DirectExchange(RabbitMQConstants.EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue questionSubmitReviewQueue() {
        return QueueBuilder.durable(RabbitMQConstants.QUEUE_QUESTION_SUBMIT_REVIEW)
                .deadLetterExchange(RabbitMQConstants.DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(DLQ)
                .build();
    }

    @Bean
    public Binding questionSubmitReviewBinding(Queue questionSubmitReviewQueue, DirectExchange saberproExchange) {
        return BindingBuilder.bind(questionSubmitReviewQueue).to(saberproExchange)
                .with(RabbitMQConstants.ROUTING_KEY_QUESTION_SUBMIT);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(RabbitMQConstants.DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Queue questionSubmitReviewDeadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public Binding questionSubmitReviewDeadLetterBinding(Queue questionSubmitReviewDeadLetterQueue,
                                                         DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(questionSubmitReviewDeadLetterQueue).to(deadLetterExchange).with(DLQ);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
