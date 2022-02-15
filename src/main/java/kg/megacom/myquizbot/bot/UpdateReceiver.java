package kg.megacom.myquizbot.bot;

import kg.megacom.myquizbot.bot.handler.Handler;
import kg.megacom.myquizbot.model.entity.User;
import kg.megacom.myquizbot.model.enums.State;
import kg.megacom.myquizbot.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
public class UpdateReceiver {
    private final List<Handler> handlers;

    private final UserRepository userRepository;

    public UpdateReceiver(List<Handler> handlers, UserRepository userRepository) {
        this.handlers = handlers;
        this.userRepository = userRepository;
    }

    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        try {
            if (isMessageWithText(update)) {
                final Message message = update.getMessage();
                final Integer chatId = message.getFrom().getId();

                final User user = userRepository.getByChatId(chatId)
                        .orElseGet(() -> userRepository.save(new User(chatId)));

                return getHandlerByState(user.getBotState()).handle(user, message.getText());
            } else if(update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                final Integer chatId = callbackQuery.getFrom().getId();
                final User user = userRepository.getByChatId(chatId)
                        .orElseGet(() -> userRepository.save(new User(chatId)));

                return getHandlerByCallBackQuery(callbackQuery.getData()).handle(user, callbackQuery.getData());
            }

            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException ex) {
            return Collections.emptyList();
        }
    }

    private Handler getHandlerByState(State state) {
        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().equals(state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query) {
        return handlers.stream()
                .filter(h -> h.operatedCallBackQuery().stream()
                        .anyMatch(query::startsWith))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
