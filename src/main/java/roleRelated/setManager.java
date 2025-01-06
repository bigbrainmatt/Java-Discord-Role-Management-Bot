package roleRelated;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static misc.ManagerManager.*;
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.*;

public class setManager extends ListenerAdapter {
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.ROLE, "role", "Mention to set as manager", true));
        data.add(new OptionData(OptionType.MENTIONABLE, "user", "User to set as manager", true));
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equalsIgnoreCase("setmanager")) {
            Role role = event.getOption("role").getAsRole();
            String user = event.getOption("user").getAsString();
            try {
                if(hasPermission(event.getGuild().getId(),event.getUser().getId(), "manage_linked_role") || hasPermission(event.getGuild().getId(),event.getUser().getId(), "guild_admin")) {
                    //if(!hasAtLeastOneManager(event.getGuild().getId(), role.getId())) {
                    // user true
                        if(determineIdType(event.getGuild(), user)){
                            if(!(getManagerForRole(event.getGuild().getId(),role.getId(),1) == null)){
                                event.reply("This is already a manger set").setEphemeral(true).queue();
                            } else {
                                addOrUpdateRole(event.getGuild().getId(),role.getId(), user,1);
                                event.reply("Manager Set").setEphemeral(true).queue();
                            }
                            embedLog(event,false, "Role Manager", event.getUser().getAsMention() + " has set the manger for " + role.getAsMention() + " to <@" + user +">");
                        } else {
                            if(!(getManagerForRole(event.getGuild().getId(),role.getId(),2) == null)){
                                addOrUpdateRole(event.getGuild().getId(),role.getId(), user,2);
                                event.reply("Manager Set").setEphemeral(true).queue();
                            } else {
                                event.reply("This is already a manger set").setEphemeral(true).queue();
                            }
                            embedLog(event,false, "Role Manager", event.getUser().getAsMention() + " has set the manger for " + role.getAsMention() + " to <@&" + user +">");

                        }

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

    public static boolean determineIdType(Guild guild, String id) {
        Member member = guild.getMemberById(id);
        if (member != null) {
            return true;
        }

        Role role = guild.getRoleById(id);
        if (role != null) {
            return false;
        }
        return false;
    }
}
