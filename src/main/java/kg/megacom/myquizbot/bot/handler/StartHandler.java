package kg.megacom.myquizbot.bot.handler;

import kg.megacom.myquizbot.model.entity.User;
import kg.megacom.myquizbot.model.enums.State;
import kg.megacom.myquizbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import static kg.megacom.myquizbot.util.TelegramUtil.createMessageTemplate;

@Component
public class StartHandler implements Handler{
        @org.springframework.beans.factory.annotation.Value("${bot.username}")
        private String botUsername;

        private final UserRepository userRepository;

        public StartHandler(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
            SendMessage welcomeMessage = createMessageTemplate(user)
                    .setText(String.format(
                            "Hola! I'm *%s*%nI am here to help you learn Java", botUsername
                    ));
            SendMessage registrationMessage = createMessageTemplate(user)
                    .setText("In order to start our journey tell me your name");
            user.setBotState(State.ENTER_NAME);
            userRepository.save(user);

            return List.of(welcomeMessage, registrationMessage);
        }

        @Override
        public State operatedBotState() {
            return State.START;
        }

        @Override
        public List<String> operatedCallBackQuery() {
            return Collections.emptyList();
        }
}
