package bot.listeners;

import bot.services.MessageService;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by Jeremy on 7/15/2017.
 */
public class MessageListener extends ListenerAdapter {
    private final MessageService messageService;

    public MessageListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String msg = message.getContent();

        TextChannel lobby = jda.getTextChannelById("282386308465164289");

        if (event.isFromType(ChannelType.TEXT) && !author.isBot()) {
            if (!msg.startsWith("!")) {
                return;
            }

            messageService.setChannel(channel);
            String args[] = msg.split(" ");
            String command = args[0].toLowerCase();
            switch (command) {
                case "!site":
                    messageService.sendSiteLink();
                    break;
                case "!discord":
                    messageService.sendDiscordLink();
                    break;
                case "!intro":
                    messageService.sendIntroLink();
                    break;
                case "!apply":
                    messageService.sendApplicationLink();
                    break;
                case "!calc":
                    String equation = msg.replace("!calc ", "");
                    messageService.sendCalculationSolution(equation);
                    break;
                case "!topic":
                    messageService.sendTopic();
                    break;
                case "!help":
                    messageService.sendHelp(channel.getName());
                    break;
                case "!tripspam":
                    messageService.sendTripSpam(lobby, channel.getName());
                    break;
                case "!spreadsheet":
                    messageService.sendSpreadSheetLink(channel.getName());
                    break;
                case "!settopic":
                    messageService.sendTopicSaved(channel.getName(), msg.replace("!setTopic ", ""));
                    break;
                case "!startspam":
                    try {
                        messageService.sendSpamStarted(args[1], lobby, channel.getName());
                    } catch (Exception e) {
                        messageService.sendMissingTimeInterval(channel.getName());
                        e.printStackTrace();
                    }
                    break;
                case "!stopspam":
                    messageService.sendStopSpam(channel.getName());
                    break;
                default:
                    break;
            }
        }
    }
}
