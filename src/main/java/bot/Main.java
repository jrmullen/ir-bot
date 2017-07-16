package bot;

import bot.listeners.MessageListener;
import bot.services.MessageService;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

/**
 * Created by Jeremy on 4/17/2017.
 */
public class Main {
    private static final String BOT_TOKEN = "MzAzNjcyNzUyMTkwMjU5MjAy.C9bjFA.bCB4rk1m6p5pfwSpE8lOdl6PUto";

    public static void main(String[] args) throws LoginException, RateLimitedException {
        MessageService messageService = new MessageService();

        JDA jda = new JDABuilder(AccountType.BOT).setToken(BOT_TOKEN)
                .addEventListener(new MessageListener(messageService)).buildAsync();
    }
}

