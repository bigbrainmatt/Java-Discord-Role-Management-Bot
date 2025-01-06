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

public class unban extends ListenerAdapter {
    private User bannedU;
    private String reason;
    private User savior;
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.USER,"member", "member to ban", true));
        data.add(new OptionData(OptionType.STRING,"reason", "reason for ban", true));
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("unban")) {
            bannedU = event.getOption("member").getAsUser();
            reason = event.getOption("reason").getAsString();
            savior = event.getUser();
            try {
                if(hasPermission(event.getGuild().getId(), event.getUser().getId(), "unban_permissions") || hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin")) {

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.red);
                    eb.setTitle("Unban member");
                    eb.setDescription("Are you sure you would like to unban " + bannedU.getAsMention() + "\n For **" + reason + "**");

                    event.replyEmbeds(eb.build())
                            .addActionRow(net.dv8tion.jda.api.interactions.components.buttons.Button.success("unbanCon","Confirm"), Button.danger("unbanDeny", "Cancel")).queue();

                } else {
                    event.replyEmbeds(getDenyEmbed().build()).setEphemeral(true).queue();
                    embedLog(event,true,"Unban Member", bannedU.getAsMention() + " has attempted to unban " + event.getUser().getAsMention() + " for **" + reason + "**");
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
        if((savior == event.getUser()) && (event.getComponentId().equalsIgnoreCase("unbanCon"))) {
            event.getInteraction().getMessage().delete().queue();

            event.getGuild().unban(bannedU).reason(reason).queue();
            embedLog(event,false,"Unban Member", savior.getAsMention() + " has unbanned " + bannedU.getAsMention() + " for **" + reason + "**");
        } if((savior == event.getUser()) && (event.getComponentId().equalsIgnoreCase("unbanDeny"))) {
            event.getInteraction().getMessage().delete().complete();
        }
    }
}
