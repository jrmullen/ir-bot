package bot.utilities;

/**
 * Created by Jeremy on 7/16/2017.
 */
public class Constants {
    public static final String CODE_START = "```";

    public static final String CODE_END = "```";

    public static final String SITE_LINK = "http://www.ir-rs.com/";

    public static final String DISCORD_LINK = "https://discord.gg/3CdzhGr";

    public static final String INTRO_LINK = "https://www.ir-rs.com/index.php?/forum/9-introductions/";

    public static final String APPLICATION_LINK = "https://www.ir-rs.com/index.php?/forum/19-applications/";

    public static final String SPREADSHEET_LINK = "redacted";

    public static final String TRIP_SPAM_HELP_TEXT = "\n\n!tripSpam        - Starts / stops the trip spam";

    public static final String HELP_TEXT = "Available commands:" +
            "\n!site            - Link for site" +
            "\n!discord         - Discord invite link" +
            "\n!intro           - Link to introductions topic" +
            "\n!apply           - Link to applications topic" +
            "\n!calc <equation> - Make calculations" +
            "\n!topic           - Display the current topic";

    public static final String TRIP_SPAM_TEXT = "```php\n\n" +
            "Change your nickname to your RSN and move to Massing voice channel\n" +
            "EXAMPLE: \"Harmz | Mike     Horror Duck | Kimmy\"\n\n" +
            "Change your NICK by typing \"/nick NEW NAME\"\n\n" +
            "If your discord name is not your RSN you will NOT be moved into events channel\n" +
            "```";

    public static final String ADMIN_HELP_TEXT = "\n\nAdmin Only:\n" +
            "!setTopic <link> - Set a topic for the !topic command\n" +
            "!startSpam X     - Starts topic spam with an interval of X minutes\n" +
            "!stopSpam        - Stops topic spam\n" +
            "!spreadsheet     - Display link to memberlist / averages spreadsheet";
}
