package misc;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static misc.PermissionManager.getPermissionValue;

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
        eb.setTitle("Insufficient permissions");
        eb.setDescription("You have insufficient permissions to run this command");

        return eb;
    }

    public static void embedLog(SlashCommandInteractionEvent event, boolean att, String type, CharSequence dis) {
        EmbedBuilder eb = new EmbedBuilder();
        if(att) {
            eb.setTitle("Attempted " + type);
        } else {
            eb.setTitle(type);
        }
        eb.setColor(getBotColor());
        eb.setDescription(dis);

        eb.setFooter(getTime(), event.getJDA().getSelfUser().getAvatarUrl());
        String chan = null;

        try {
            chan = getPermissionValue(event.getGuild().getId(), "0", "SERVER_LOG_CHAN");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        event.getGuild().getTextChannelById(chan).sendMessageEmbeds(eb.build()).queue();
    }

    public static void embedLog(ButtonInteractionEvent event, boolean att, String type, CharSequence dis) {
        EmbedBuilder eb = new EmbedBuilder();
        if(att) {
            eb.setTitle("Attempted " + type);
        } else {
            eb.setTitle(type);
        }
        eb.setColor(getBotColor());
        eb.setDescription(dis);

        eb.setFooter(getTime(), event.getJDA().getSelfUser().getAvatarUrl());
        String chan = null;

        try {
            chan = getPermissionValue(event.getGuild().getId(), "0", "SERVER_LOG_CHAN");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        event.getGuild().getTextChannelById(chan).sendMessageEmbeds(eb.build()).queue();
    }

}
