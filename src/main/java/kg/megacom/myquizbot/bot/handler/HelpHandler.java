package kg.megacom.myquizbot.bot.handler;

import kg.megacom.myquizbot.model.entity.User;
import kg.megacom.myquizbot.model.enums.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static kg.megacom.myquizbot.bot.handler.RegistrationHandler.NAME_CHANGE;
import static kg.megacom.myquizbot.util.TelegramUtil.createMessageTemplate;
import static kg.megacom.myquizbot.util.TelegramUtil.createInlineKeyboardButton;

@Component
public class HelpHandler implements Handler{

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Change name", NAME_CHANGE));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user).setText(String.format("" +
                        "You've asked for help %s? Here it comes!", user.getName()))
                .setReplyMarkup(inlineKeyboardMarkup));

    }

    @Override
    public State operatedBotState() {
        return State.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
