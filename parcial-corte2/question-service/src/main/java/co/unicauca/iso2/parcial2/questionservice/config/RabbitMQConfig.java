/**
 * @file RabbitMQConfig.java
 * @brief Topología RabbitMQ del lado productor.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.config;

import co.unicauca.iso2.parcial2.common.constants.RabbitMQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declara exchange, cola y binding también en el productor (con los MISMOS argumentos
 * que lifecycle-service) para que no se pierdan mensajes si se envía una pregunta a
 * revisión antes de que el consumidor haya arrancado.
 */
@Configuration
public class RabbitMQConfig {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Bean
    public DirectExchange saberproExchange() {
        return new DirectExchange(RabbitMQConstants.EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue questionSubmitReviewQueue() {
        return QueueBuilder.durable(RabbitMQConstants.QUEUE_QUESTION_SUBMIT_REVIEW)
                .deadLetterExchange(RabbitMQConstants.DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(RabbitMQConstants.QUEUE_QUESTION_SUBMIT_REVIEW + RabbitMQConstants.DEAD_LETTER_SUFFIX)
                .build();
    }

    @Bean
    public Binding questionSubmitReviewBinding(Queue questionSubmitReviewQueue, DirectExchange saberproExchange) {
        return BindingBuilder.bind(questionSubmitReviewQueue).to(saberproExchange)
                .with(RabbitMQConstants.ROUTING_KEY_QUESTION_SUBMIT);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(returned -> log.warn(
                "Mensaje no enrutado: exchange={} routingKey={} motivo={}",
                returned.getExchange(), returned.getRoutingKey(), returned.getReplyText()));
        return rabbitTemplate;
    }
}
