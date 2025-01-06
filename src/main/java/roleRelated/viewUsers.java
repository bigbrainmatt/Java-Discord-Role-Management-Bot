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
import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.*;


public class viewUsers extends ListenerAdapter {

    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.ROLE, "role", "Mention to get perms", true));
        data.add(new OptionData(OptionType.BOOLEAN, "hidden", "Visibility or response", true));
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("viewusers")) {
            Guild guild = event.getGuild();
            try {
                if(hasPermission(guild.getId(),event.getUser().getId(),"manage_role_permissions") || hasPermission(guild.getId(),event.getUser().getId(),"guild_admin")){
                    Role role = event.getOption("role").getAsRole();
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(getBotColor());
                    eb.setTitle("Memebers");
                    String dis = "Users with " + role.getAsMention() + " role\n";

                    List<Member> members = event.getGuild().getMembersWithRoles(role);

                    for (Member member : members) {
                        dis+= member.getAsMention() +"\n";
                    }
                    eb.setDescription(dis);

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
