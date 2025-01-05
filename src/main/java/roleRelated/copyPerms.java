package roleRelated;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static misc.PermissionManager.*;
import static misc.miscInfo.*;

public class copyPerms extends ListenerAdapter {
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.ROLE,"role1", "Role to copy", true));
        data.add(new OptionData(OptionType.ROLE,"role2", "Role to copy to", true));
        return data;
    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("copyperms")) {
            try {
                if(hasPermission(event.getGuild().getId(), event.getUser().getId(), "manage_role_permissions") || hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin")) {
                    Role role1 = event.getOption("role1").getAsRole();
                    Role role2 = event.getOption("role2").getAsRole();

                    if(!rowExistsById(event.getGuild().getId(), role1.getId())){
                        addPermission(event.getGuild().getId(),role1.getId(),"SERVER_LOG_CHAN","0");
                    }
                    if(!rowExistsById(event.getGuild().getId(), role2.getId())) {
                        addPermission(event.getGuild().getId(),role1.getId(),"SERVER_LOG_CHAN","0");
                    }

                    copyValue(event.getGuild().getId(), role1.getId(), role2.getId());

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Permissions Copied");
                    eb.setColor(getBotColor());
                    eb.setDescription(role1.getAsMention() + " permissions have now been copied to " + role2.getAsMention());

                    event.replyEmbeds(eb.build()).setEphemeral(true).queue();

                    eb.setDescription(event.getUser().getAsMention() + "has copied the permissions from " + role1.getAsMention() + " to " + role2.getAsMention());

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
}
