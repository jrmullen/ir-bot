package bot;

import bot.listeners.MessageListener;
import bot.services.MessageService;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Jeremy on 4/17/2017.
 */
public class Main {

    public static void main(String[] args) throws LoginException, RateLimitedException, InterruptedException {
        MessageService messageService = new MessageService();
        Properties properties = new Properties();
        try {
            InputStream input = new FileInputStream("config.properties");
            properties.load(input);
            String token = properties.getProperty("token");

            JDA jda = new JDABuilder(AccountType.BOT).setToken(token)
                    .addEventListener(new MessageListener(messageService)).buildAsync();

        } catch (IOException e) {
            System.out.println("Error reading properties");
        }
    }
}

