package bot.services;

import bot.utilities.Constants;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Timer;

/**
 * Created by Jeremy on 7/15/2017.
 */
public class MessageService {
    private MessageChannel channel;
    private Timer tripTimer;
    private SpamTimerService spamTimer;
    private boolean tripSpamming = false;
    private boolean topicSpamming = false;
    private String savedTopic = "";
    private int spamFrequency;

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public void sendSiteLink() {
        channel.sendMessage(Constants.SITE_LINK).queue();
    }

    public void sendIntroLink() {
        channel.sendMessage(Constants.INTRO_LINK).queue();
    }

    public void sendApplicationLink() {
        channel.sendMessage(Constants.APPLICATION_LINK).queue();
    }

    public void sendDiscordLink() {
        channel.sendMessage(Constants.DISCORD_LINK).queue();
    }

    public void sendCalculationSolution(String equation) {
        channel.sendMessage(String.valueOf(CalculatorService.eval(equation))).queue();
    }

    public void sendTopic() {
        try {
            if (!savedTopic.equals("")) {
                channel.sendMessage(savedTopic).queue();
            } else {
                channel.sendMessage("There is no topic currently set").queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHelp(String channelName) {
        if (channelName.equals("admin") || channelName.equals("internal")) {
            channel.sendMessage(Constants.CODE_START + Constants.HELP_TEXT + Constants.TRIP_SPAM_HELP_TEXT
                    + Constants.ADMIN_HELP_TEXT + Constants.CODE_END).queue();
        } else if (channelName.equals("scouting") || channelName.equals("intel")) {
            channel.sendMessage(Constants.CODE_START + Constants.HELP_TEXT + Constants.TRIP_SPAM_HELP_TEXT + Constants.CODE_END).queue();
        } else {
            channel.sendMessage(Constants.CODE_START + Constants.HELP_TEXT + Constants.CODE_END).queue();
        }
    }

    public void sendTripSpam(TextChannel lobby, String channelName) {
        if (channelName.equals("admin")
                || channelName.equals("internal")
                || channelName.equals("intel")
                || channelName.equals("scouting")) {
            if (!tripSpamming) {
                SpamTimerService tripSpamTimer = new SpamTimerService(MessageService.this, Constants.TRIP_SPAM_TEXT, lobby);
                tripTimer = new Timer();
                tripSpamming = true;
                channel.sendMessage("Trip spam started").queue();
                tripTimer.schedule(tripSpamTimer, 0, 1000 * 60 * 5);
            } else {
                tripSpamming = false;
                tripTimer.cancel();
                channel.sendMessage("Trip spam stopped").queue();
            }
        }
    }

    public void sendSpreadSheetLink(String channelName) {
        if (channelName.equals("admin") || channelName.equals("internal")) {
            channel.sendMessage(Constants.SPREADSHEET_LINK).queue();
        }
    }

    public void sendTopicSaved(String channelName, String topicString) {
        if (channelName.equals("admin") || channelName.equals("internal")) {
            savedTopic = topicString;
            channel.sendMessage("Topic saved as:\n" + topicString).queue();
        }
    }

    public void sendSpamStarted(String timeInterval, MessageChannel lobby, String channelName) {
        if (channelName.equals("admin") || channelName.equals("internal")) {
            try {
                spamFrequency = Integer.parseInt(timeInterval);
            } catch (Exception e) {
                channel.sendMessage("Error: " + timeInterval + " must be an integer.").queue();
                e.printStackTrace();
                return;
            }

            if (!topicSpamming) {
                if (savedTopic.equals("")) {
                    channel.sendMessage("There is no topic currently set. Set one using the !setTopic command").queue();
                } else {
                    spamTimer = new SpamTimerService(MessageService.this, savedTopic, lobby);
                    Timer timer = new Timer();
                    topicSpamming = true;
                    String message = "Spam started with time interval: " + spamFrequency + " minute";
                    if (spamFrequency == 1) {
                        channel.sendMessage(message).queue();
                    } else {
                        channel.sendMessage(message.concat("s")).queue();
                    }
                    timer.schedule(spamTimer, 0, 1000 * 60 * spamFrequency);
                }
            } else {
                channel.sendMessage("There is already a spam set. Use the !stopSpam command to stop it.").queue();
            }
        }
    }

    public void sendStopSpam(String channelName) {
        if (channelName.equals("admin") || channelName.equals("internal")) {
            if (topicSpamming) {
                topicSpamming = false;
                spamTimer.cancel();
                channel.sendMessage("Spam stopped").queue();
            } else {
                channel.sendMessage("No spam currently set").queue();
            }
        }
    }

    public void sendSpamMessage(String message, MessageChannel channel) {
        channel.sendMessage(message).queue();
    }

    public void sendMissingTimeInterval(String channelName) {
        if (channelName.equals("admin") || channelName.equals("internal")) {
            channel.sendMessage("Error: Please provide a time interval for the spam").queue();
        }
    }
}
