package misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class miscInfo {
    public static Color getBotColor() {
        return new Color(137, 158, 158);
    }

    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateTime = dateFormat.format(new Date());
        return dateTime;
    }

    public static EmbedBuilder getDenyEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.red);
        eb.setTitle("Kick member");
        eb.setDescription("You have insufficient permissions to run this command");

        return eb;
    }

}
