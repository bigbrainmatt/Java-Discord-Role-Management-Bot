package roleRelated;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import static misc.ManagerManager.addOrUpdateRole;
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.embedLog;
import static misc.miscInfo.getDenyEmbed;

public class createRole extends ListenerAdapter {
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.STRING, "rolename", "Name for role", true));
        data.add(new OptionData(OptionType.STRING, "rolecolor", "Using hex no #", true));
        data.add(new OptionData(OptionType.ROLE, "placeunder", "Where to place role", true));
        data.add(new OptionData(OptionType.USER, "rolemanager", "Where to place role", true));
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("createrole")) {
            try {
                String name = event.getOption("rolename").getAsString();
                String color = event.getOption("rolecolor").getAsString();
                Role pUnder = event.getOption("placeunder").getAsRole();
                User user = event.getOption("rolemanager").getAsUser();

                if (hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin") || hasPermission(event.getGuild().getId(), event.getUser().getId(), "create_role")) {
                    Role nR = event.getGuild().createRole().setName(name).setColor(HexFormat.fromHexDigits(color)).complete();

                    //changeRoleHierarchy(event.getGuild(), nR, pUnder.getPosition() + 1);

                    addOrUpdateRole(event.getGuild().getId(), nR.getId(), user.getId(), 1);
                    embedLog(event, false, "Role Created", event.getUser().getAsMention() + " has created a a role " + nR.getAsMention() + " with a manager " + user.getAsMention());
                    event.reply("Role successfully created").setEphemeral(true).queue();
                } else {
                    event.replyEmbeds(getDenyEmbed().build()).queue();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void changeRoleHierarchy(Guild guild, Role roleToMove, int newPosition) {
        guild.modifyRolePositions()
                .selectPosition(roleToMove)
                .moveTo(newPosition)
                .queue(
                        success -> System.out.println("Role position updated successfully!"),
                        error -> System.err.println("Failed to update role position: " + error.getMessage())
                );
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        changeRoleHierarchy(event.getGuild(), event.getGuild().getRoleById("1326020147747491933"),event.getGuild().getRoleById("1325655621391089685").getPosition());
    }
}
