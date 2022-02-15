package kg.megacom.myquizbot.bot.handler;

import kg.megacom.myquizbot.model.entity.Question;
import kg.megacom.myquizbot.model.entity.User;
import kg.megacom.myquizbot.model.enums.State;
import kg.megacom.myquizbot.repository.QuestionRepository;
import kg.megacom.myquizbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kg.megacom.myquizbot.util.TelegramUtil.createMessageTemplate;
import static kg.megacom.myquizbot.util.TelegramUtil.createInlineKeyboardButton;

@Component
@Slf4j
public class QuizHandler implements Handler{
    public static final String QUIZ_CORRECT = "/quiz_correct";
    public static final String QUIZ_INCORRECT = "/quiz_incorrect";
    public static final String QUIZ_START = "/quiz_start";
    private static final List<String> OPTIONS = List.of("A", "B", "C", "D");

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public QuizHandler(UserRepository userRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        if (message.startsWith(QUIZ_CORRECT)) {
            return correctAnswer(user, message);
        } else if (message.startsWith(QUIZ_INCORRECT)) {
            return incorrectAnswer(user);
        } else {
            return startNewQuiz(user);
        }
    }

    private List<PartialBotApiMethod<? extends Serializable>> correctAnswer(User user, String message) {
        log.info("correct");
        final int currentScore = user.getScore() + 1;
        user.setScore(currentScore);
        userRepository.save(user);

        return nextQuestion(user);
    }

    private List<PartialBotApiMethod<? extends Serializable>> incorrectAnswer(User user) {
        final int currentScore = user.getScore();
        if (user.getHighScore() < currentScore) {
            user.setHighScore(currentScore);
        }
        user.setScore(0);
        user.setBotState(State.NONE);
        userRepository.save(user);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Try again?", QUIZ_START));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user)
                .setText(String.format("Incorrect!%nYou scored *%d* points!", currentScore))
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    private List<PartialBotApiMethod<? extends Serializable>> startNewQuiz(User user) {
        user.setBotState(State.PLAYING_QUIZ);
        userRepository.save(user);

        return nextQuestion(user);
    }

    private List<PartialBotApiMethod<? extends Serializable>> nextQuestion(User user) {
        Question question = questionRepository.getRandomQuestion();

        List<String> options = new ArrayList<>(List.of(question.getCorrectAnswer(), question.getOptionOne(), question.getOptionTwo(), question.getOptionThree()));

        Collections.shuffle(options);

        StringBuilder sb = new StringBuilder();
        sb.append('*')
                .append(question.getQuestion())
                .append("*\n\n");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowTwo = new ArrayList<>();

        for (int i = 0; i < options.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();

            final String callbackData = options.get(i).equalsIgnoreCase(question.getCorrectAnswer()) ? QUIZ_CORRECT : QUIZ_INCORRECT;

            button.setText(OPTIONS.get(i))
                    .setCallbackData(String.format("%s %d", callbackData, question.getId()));

            if (i < 2) {
                inlineKeyboardButtonsRowOne.add(button);
            } else {
                inlineKeyboardButtonsRowTwo.add(button);
            }
            sb.append(OPTIONS.get(i) + ". " + options.get(i));
            sb.append("\n");
        }

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne, inlineKeyboardButtonsRowTwo));
        return List.of(createMessageTemplate(user)
                .setText(sb.toString())
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public State operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(QUIZ_START, QUIZ_CORRECT, QUIZ_INCORRECT);
    }
}
