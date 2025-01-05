package modderation;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static misc.PermissionManager.getPermissionValue;
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.*;

public class ban extends ListenerAdapter {
    private static User bannedUser;
    private static String reason;
    private static User punisher;

    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.USER,"member", "member to ban", true));
        data.add(new OptionData(OptionType.STRING,"reason", "reason for ban", true));
        return data;
    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("ban")) {
            try {
                if(hasPermission(event.getGuild().getId(), event.getUser().getId(), "ban_permissions") || hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin")) {
                    bannedUser = event.getOption("member").getAsUser();
                    reason = event.getOption("reason").getAsString();
                    punisher = event.getUser();

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.red);
                    eb.setTitle("Kick member");
                    eb.setDescription("Are you sure you would like to ban " + bannedUser.getAsMention() + "\n For **" + reason + "**");

                    event.replyEmbeds(eb.build())
                            .addActionRow(net.dv8tion.jda.api.interactions.components.buttons.Button.success("banCon","Confirm"), Button.danger("banDeny", "Cancel")).queue();

                } else {
                    event.replyEmbeds(getDenyEmbed().build()).setEphemeral(true).queue();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if((punisher == event.getUser()) && (event.getComponentId().equalsIgnoreCase("banCon"))) {
            event.getInteraction().getMessage().delete().queue();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Member Banned");
            eb.setColor(getBotColor());
            eb.setDescription(bannedUser.getAsMention() + " has been banned by " + event.getUser().getAsMention() + " for **" + reason + "**");

            //event.getGuild().ban(bannedUser,0, TimeUnit.DAYS).reason(reason).complete();

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
        } if((punisher == event.getUser()) && (event.getComponentId().equalsIgnoreCase("banDeny"))) {
            event.getInteraction().getMessage().delete().complete();
        }
    }
}
