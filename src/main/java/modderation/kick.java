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

import static misc.PermissionManager.getPermissionValue;
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.*;

public class kick extends ListenerAdapter {

    private static User kickedU;
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
        if (event.getName().equalsIgnoreCase("kick")) {
            kickedU = event.getOption("member").getAsUser();
            reason = event.getOption("reason").getAsString();
            punisher = event.getUser();
            try {
                if(hasPermission(event.getGuild().getId(), event.getUser().getId(), "kick_permissions") || hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin")) {

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.red);
                    eb.setTitle("Kick member");
                    eb.setDescription("Are you sure you would like to kick " + kickedU.getAsMention() + "\n For **" + reason + "**");

                    event.replyEmbeds(eb.build())
                            .addActionRow(net.dv8tion.jda.api.interactions.components.buttons.Button.success("kickCon","Confirm"), Button.danger("kickDeny", "Cancel")).queue();

                } else {
                    event.replyEmbeds(getDenyEmbed().build()).setEphemeral(true).queue();
                    embedLog(event,true,"Kick Member", punisher.getAsMention() + " has attempted to kick " + kickedU.getAsMention() + " for **" + reason + "**");
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
        if((punisher == event.getUser()) && (event.getComponentId().equalsIgnoreCase("kickCon"))) {
            event.getInteraction().getMessage().delete().queue();

            event.getGuild().kick(kickedU).queue();
            embedLog(event,false,"Kick Member", punisher.getAsMention() + " has kick " + kickedU.getAsMention() + " for **" + reason + "**");
        } if((punisher == event.getUser()) && (event.getComponentId().equalsIgnoreCase("kickDeny"))) {
            event.getInteraction().getMessage().delete().complete();
        }
    }

}
