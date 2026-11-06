/**
 * @file QuestionDomainMapper.java
 * @brief Mapeador bidireccional Question <-> QuestionRecord (Punto 3.3).
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.mapper;

import co.unicauca.iso2.parcial2.questionservice.domain.model.AnswerOption;
import co.unicauca.iso2.parcial2.questionservice.domain.model.Question;
import co.unicauca.iso2.parcial2.questionservice.domain.model.QuestionStatus;
import co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.record.AnswerOptionRecord;
import co.unicauca.iso2.parcial2.questionservice.infrastructure.jpa.record.QuestionRecord;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * @brief Aísla el modelo de dominio de los registros de base de datos.
 *
 * Es la frontera que permite que Question siga siendo Java puro.
 */
@Component
public class QuestionDomainMapper {

    /** Dominio -> Record (para guardar). */
    public QuestionRecord toRecord(Question domain) {
        if (domain == null) return null;

        QuestionRecord record = new QuestionRecord();
        record.setId(domain.getId());
        record.setText(domain.getText());
        record.setJustification(domain.getJustification());
        record.setAuthorId(domain.getAuthorId());
        record.setStatus(domain.getStatus().name());
        for (AnswerOption option : domain.getOptions()) {
            record.addOption(toRecord(option));
        }
        return record;
    }

    /** Record -> Dominio (para leer). */
    public Question toDomain(QuestionRecord record) {
        if (record == null) return null;

        List<AnswerOption> options = record.getOptions().stream()
                .sorted(Comparator.comparing(AnswerOptionRecord::getOptionLabel))
                .map(this::toDomain)
                .toList();
        return Question.restore(record.getId(), record.getText(), record.getJustification(),
                record.getAuthorId(), QuestionStatus.valueOf(record.getStatus()), options);
    }

    public AnswerOptionRecord toRecord(AnswerOption domain) {
        return new AnswerOptionRecord(domain.getId(), domain.getText(), domain.isCorrect());
    }

    public AnswerOption toDomain(AnswerOptionRecord record) {
        return new AnswerOption(record.getOptionLabel(), record.getText(), record.isCorrect());
    }
}
