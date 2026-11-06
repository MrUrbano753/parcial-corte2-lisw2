/**
 * @file ReviewRequestPublisher.java
 * @brief Puerto para publicar el comando de envío a revisión.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.domain.port;

import co.unicauca.iso2.parcial2.common.messages.SubmitQuestionForReview;

/** Lo implementa RabbitReviewRequestPublisher (Punto 2.2). */
public interface ReviewRequestPublisher {

    void publish(SubmitQuestionForReview command);
}
