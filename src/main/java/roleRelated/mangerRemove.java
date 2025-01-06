package roleRelated;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static misc.ManagerManager.removeManagerFromRole;
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.embedLog;
import static misc.miscInfo.getDenyEmbed;

public class mangerRemove extends ListenerAdapter {
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.ROLE, "role", "Mention to get perms", true));
        data.add(new OptionData(OptionType.STRING, "choices", "Remove or add", true)
                .addChoice("userslot", "userslot")
                .addChoice("roleslot", "roleslot")
        );
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equalsIgnoreCase("removemanager")) {
            try {
                if(hasPermission(event.getGuild().getId(),event.getUser().getId(), "manage_linked_role") || hasPermission(event.getGuild().getId(),event.getUser().getId(), "guild_admin")) {
                    Role role = event.getOption("role").getAsRole();
                    if (event.getOption("choices").getAsString().equalsIgnoreCase("userslot")) {
                        removeManagerFromRole(event.getGuild().getId(), role.getId(), 1);
                    } else if (event.getOption("choices").getAsString().equalsIgnoreCase("roleslot")) {
                        removeManagerFromRole(event.getGuild().getId(), role.getId(), 2);
                    }
                    event.reply("Manager has been removed").setEphemeral(true).queue();
                    embedLog(event,false,"Manager Removed", event.getUser().getAsMention() + " has removed a manager from " + event.getOption("role").getAsRole().getAsMention());
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
