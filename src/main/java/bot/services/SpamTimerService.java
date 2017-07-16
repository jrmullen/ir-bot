package bot.services;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.TimerTask;

/**
 * Created by Jeremy on 4/19/2017.
 */
public class SpamTimerService extends TimerTask {
    private MessageService messageService;
    private String message;
    private MessageChannel channel;

    public SpamTimerService(MessageService messageService,
                            String message,
                            MessageChannel channel) {
        this.messageService = messageService;
        this.message = message;
        this.channel = channel;
    }

    @Override
    public void run() {
        messageService.sendSpamMessage(message, channel);
    }
}
