import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotChanged extends TelegramLongPollingBot {

    ArrayList events = new ArrayList();


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new BotChanged());
        }
        catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start" : sendMsg(message, "Ну что ж, приступим. Прошу тебя ознакомиться с моей работой. Для этого выбери команду /help");
                break;
                case "/help" : sendMsg(message,"Всё очень просто. Введи два (!!!) события отдельными сообщениями и жми 'Подбросить монетку' \uD83D\uDE09" );
                break;

                case "Подбросить монетку" : {
                    if (events.size() == 0){
                        sendMsg(message, "Прежде чем бросить монетку, введи два (!!!) события.");
                    }
                    else if (events.size() == 1) {
                        sendMsg(message, "Подбрасывать монетку с одним событием? Хитро, хитро. Введи второе событие.");
                    }
                    else {
                        Random random = new Random();
                        int number = random.nextInt(2);

                        sendMsgWithReply(message, events.get(number).toString());
                        events.clear();
                    }
                    break;
                }
                default: {
                    if (events.size() >= 2) {
                        events.clear();
                        sendMsg(message, "Вы ввели более 2-ух событий. Они были стёрты, заполните ввод заново.");
                    }
                    else if (events.size() == 0) {
                        sendMsg(message, "Отлично! Введи второе событие.");
                        events.add(message.getText());
                    }
                    else if (events.size() == 1) {
                        sendMsg(message, "Отлично! Жми 'Подбросить монетку'");
                        events.add(message.getText());
                    }
                    break;
                }
            }
        }
    }
    

    public String getBotUsername() {
        return "CoinFlipUK_bot";
    }
    public String getBotToken() {
        return "1017778448:AAHq3ZID-09njy5iGPTZVMP_RF_uVnCI_Zc";
    }


    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        //sendMessage.setReplyToMessageId(message.getMessageId());   // reply on your message
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsgWithReply(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        sendMessage.setReplyToMessageId(message.getMessageId());   // reply on your message
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);  // для всех ли кнопка
        replyKeyboardMarkup.setResizeKeyboard(true);  // автоматическое раширение кнопок
        replyKeyboardMarkup.setOneTimeKeyboard(false); // false что бы не скрывать кнопки после использования

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow(); // строка кнопок
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("Подбросить монетку"));
        keyboardSecondRow.add(new KeyboardButton("/help"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}

