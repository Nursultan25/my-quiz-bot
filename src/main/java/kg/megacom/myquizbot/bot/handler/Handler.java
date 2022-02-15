package kg.megacom.myquizbot.bot.handler;

import kg.megacom.myquizbot.model.entity.User;
import kg.megacom.myquizbot.model.enums.State;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

public interface Handler {
    List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message);
    State operatedBotState();
    List<String> operatedCallBackQuery();
}
