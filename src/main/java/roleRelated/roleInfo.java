package roleRelated;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static misc.ManagerManager.getFormattedManagerIds;
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.getBotColor;
import static misc.miscInfo.getDenyEmbed;

public class roleInfo extends ListenerAdapter {

    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.ROLE, "role", "Mention to get perms", true));
        data.add(new OptionData(OptionType.BOOLEAN, "hidden", "Visibility or response", true));
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("roleinfo")) {
            Guild guild = event.getGuild();
            try {
                if(hasPermission(guild.getId(),event.getUser().getId(),"manage_role_permissions") || hasPermission(guild.getId(),event.getUser().getId(),"guild_admin")){
                    Role role = event.getOption("role").getAsRole();
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(getBotColor());
                    eb.setTitle("Role Info");
                    String dis = "";
                    eb.addField("Role Name", role.getAsMention(),true);
                    eb.addField("Role ID", "``" + role.getId() + "``",true);
                    List<Member> members = event.getGuild().getMembersWithRoles(role);
                    eb.addField("Users", "``" +members.size()+"``",true);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    eb.addField("Creation Date",role.getTimeCreated().format(formatter),true);
                    eb.addField("Managers",
                            getFormattedManagerIds(event.getGuild().getId(), role.getId())
                            ,true);


                    if (event.getOption("hidden").getAsBoolean()) {
                        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                    } else {
                        event.replyEmbeds(eb.build()).setEphemeral(false).queue();
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


}
