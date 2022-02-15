package kg.megacom.myquizbot.model.entity;

import com.sun.istack.NotNull;
import kg.megacom.myquizbot.model.enums.State;
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
@Table(name = "tb_user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{
    @Column(name = "chat_id", unique = true, nullable = false)
    @NotNull
    private Integer chatId;

    @Column(name = "name", unique = true, nullable = false)
    @NotBlank
    private String name;

    @Column(name = "score", nullable = false)
    @NotNull
    private Integer score;

    @Column(name = "high_score", nullable = false)
    @NotNull
    private Integer highScore;

    @Column(name = "bot_state", nullable = false)
    @NotBlank
    private State botState;

    public User(Integer chatId) {
        this.chatId = chatId;
        this.name = String.valueOf(chatId);
        this.score = 0;
        this.highScore = 0;
        this.botState = State.START;
    }
}
