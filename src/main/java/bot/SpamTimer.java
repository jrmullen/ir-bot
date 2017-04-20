package bot;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.TimerTask;

/**
 * Created by Jeremy on 4/19/2017.
 */

public class SpamTimer extends TimerTask {

    private MessageChannel channel;
    private String topicLink;

    public SpamTimer(MessageChannel channel, String topicLink) {
        this.channel = channel;
        this.topicLink = topicLink;
    }

    @Override
    public void run() {
        postMessage();
    }

    private void postMessage() {
        channel.sendMessage(topicLink).queue();
    }
}
