package kg.megacom.myquizbot.bot.handler;

import kg.megacom.myquizbot.model.entity.User;
import kg.megacom.myquizbot.model.enums.State;
import kg.megacom.myquizbot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.List;

import static kg.megacom.myquizbot.bot.handler.QuizHandler.QUIZ_START;
import static kg.megacom.myquizbot.util.TelegramUtil.createMessageTemplate;
import static kg.megacom.myquizbot.util.TelegramUtil.createInlineKeyboardButton;

@Component
public class RegistrationHandler implements Handler{
    public static final String NAME_ACCEPT = "/enter_name_accept";
    public static final String NAME_CHANGE = "/enter_name";
    public static final String NAME_CHANGE_CANCEL = "/enter_name_cancel";

    private final UserRepository userRepository;

    public RegistrationHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        if (message.equalsIgnoreCase(NAME_ACCEPT) || message.equalsIgnoreCase(NAME_CHANGE_CANCEL)) {
            return accept(user);
        } else if (message.equalsIgnoreCase(NAME_CHANGE)) {
            return changeName(user);
        }
        return checkName(user, message);

    }

    private List<PartialBotApiMethod<? extends Serializable>> accept(User user) {
        user.setBotState(State.NONE);
        userRepository.save(user);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Start quiz", QUIZ_START));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user).setText(String.format(
                        "Your name is saved as: %s", user.getName()))
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    private List<PartialBotApiMethod<? extends Serializable>> checkName(User user, String message) {
        user.setName(message);
        userRepository.save(user);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Accept", NAME_ACCEPT));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user)
                .setText(String.format("You have entered: %s%nIf this is correct - press the button", user.getName()))
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    private List<PartialBotApiMethod<? extends Serializable>> changeName(User user) {
        user.setBotState(State.ENTER_NAME);
        userRepository.save(user);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Cancel", NAME_CHANGE_CANCEL));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user).setText(String.format(
                        "Your current name is: %s%nEnter new name or press the button to continue", user.getName()))
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public State operatedBotState() {
        return State.ENTER_NAME;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(NAME_ACCEPT, NAME_CHANGE, NAME_CHANGE_CANCEL);
    }
}

