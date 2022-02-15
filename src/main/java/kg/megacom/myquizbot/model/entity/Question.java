package kg.megacom.myquizbot.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "tb_question")
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseEntity{
    @Column(name = "question", nullable = false)
    @NotBlank
    private String question;

    @Column(name = "answer_correct", nullable = false)
    @NotBlank
    private String correctAnswer;

    @Column(name = "option2", nullable = false)
    @NotBlank
    private String optionOne;

    @Column(name = "option1", nullable = false)
    @NotBlank
    private String optionTwo;

    @Column(name = "option3", nullable = false)
    @NotBlank
    private String optionThree;
}
