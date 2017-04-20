package bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.Timer;

/**
 * Created by Jeremy on 4/17/2017.
 */

public class ReadyListener extends ListenerAdapter {

    private static final String BOT_TOKEN = "MzAzNjcyNzUyMTkwMjU5MjAy.C9bjFA.bCB4rk1m6p5pfwSpE8lOdl6PUto";
    private static Timer timer = new Timer();
    private String topicLink = "";

    public static void main(String[] args) throws LoginException, RateLimitedException {
        JDA jda = new JDABuilder(AccountType.BOT).setToken(BOT_TOKEN)
                .addEventListener(new ReadyListener()).buildAsync();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();

        long responseNumber = event.getResponseNumber(); //The amount of discord events that JDA has received since the last reconnect

        //Event specific information
        User author = event.getAuthor();                //The user that sent the message
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
                                                        //  This could be a TextChannel, PrivateChannel, or Group!
        String msg = message.getContent();
        boolean bot = author.isBot();
        TextChannel textChannel = event.getTextChannel();

        TextChannel lobby = jda.getTextChannelById("282386308465164289");
        SpamTimer spamTimer = new SpamTimer(lobby, topicLink);

        if (event.isFromType(ChannelType.TEXT) && !author.isBot()) { //If this message was sent to a Guild TextChannel

            if (msg.equals("!site")) {
                channel.sendMessage("http://www.ir-rs.com/").queue();
            }

            if (msg.equals("!invite")) {
                channel.sendMessage("https://discord.gg/3CdzhGr").queue();
            }

            if (msg.startsWith("!calc ")) {
                String equation = msg.replace("!calc ", "");
                channel.sendMessage(String.valueOf(eval(equation))).queue();
            }

            if (msg.equals("!topic")) {
                try {
                    if (!topicLink.equals("")) {
                        channel.sendMessage(topicLink).queue();
                    } else {
                        channel.sendMessage("There is no topic currently set").queue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (msg.equals("!help")) {
                channel.sendMessage("Available commands: " +
                        "\n!site - Link for site" +
                        "\n!invite - Discord invite link" +
                        "\n!calc <equation> - make calculations" +
                        "\n!topic - display the current topic").queue();
            }
        }

        if (event.isFromType(ChannelType.TEXT) && event.isFromType(ChannelType.TEXT) && event.getTextChannel().getName().equals("admin")
                || event.isFromType(ChannelType.TEXT) && event.getTextChannel().getName().equals("council")
                || event.isFromType(ChannelType.TEXT) && event.getTextChannel().getName().equals("tempadmin")
                && !author.isBot()) {
            if (msg.equals("!spreadsheet")) {
                channel.sendMessage("https://docs.google.com/spreadsheets/d/1jujHXpdEWATecnv3-yW1VVMkvK43rt7CVUyygKWm1Ko/edit?usp=sharing").queue();
            }

            if (msg.equals("!startSpam")) {
                if (topicLink.equals("")) {
                    channel.sendMessage("There is no topic currently set").queue();
                } else {
                    channel.sendMessage("Spam started").queue();
                    // schedule the task to run starting now and then every hour...
                    timer.schedule (spamTimer, 0, 1000*60*60);
                }
            }

            if (msg.equals("!stopSpam")) {
                timer.cancel();
                channel.sendMessage("Spam stopped").queue();
            }

            if (msg.startsWith("!setTopic ")) {
                topicLink = msg.replace("!setTopic ", "");
                channel.sendMessage("Topic saved as " + topicLink).queue();
            }

            if (msg.equals("!help")) {
                channel.sendMessage("ADMIN ONLY" +
                        "\n!setTopic <link> - set a topic for the !topic command" +
                        "\n!startSpam - starts spam (posts the current topic once every hour)" +
                        "\n!stopSpam - stops spam (stops posting the current topic once every hour)" +
                        "\n!spreadsheet - display link to memberlist / averages spreadsheet").queue();
            }
        }
    }

    //credit to @Boann on StackOverflow http://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}

