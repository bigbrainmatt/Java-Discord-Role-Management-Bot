package roleRelated;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static misc.PermissionManager.*;
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.getBotColor;
import static misc.miscInfo.getDenyEmbed;

public class listPerms extends ListenerAdapter {
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.MENTIONABLE, "mention", "Mention to get perms", true));
        data.add(new OptionData(OptionType.BOOLEAN, "hidden", "Visibility or response", true));
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("listperms")) {
            try {
                if (hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin")) {

                    EmbedBuilder eb;
                    try {
                        eb = getPermEmbed(event, event.getOption("mention").getAsString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (CsvException e) {
                        throw new RuntimeException(e);
                    }

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

    public static EmbedBuilder getPermEmbed(SlashCommandInteractionEvent event, String id) throws IOException, CsvException {
        String guildId = event.getGuild().getId();
        String pID = id;
        boolean isRole = false;

        try {
            pID = event.getGuild().getRoleById(pID).getId();
            isRole = true;
        } catch (Exception e) {
            try {
                pID = event.getGuild().getMemberById(pID).getId();
            } catch (Exception x) {

            }
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(getBotColor());
        if(isRole) {
            eb.setTitle("Role Permissions");
            eb.setDescription("Here are the permissions for <@&" + pID +">");
        } else {
            eb.setTitle("User Permissions");
            eb.setDescription("Here are the permissions for <@" + pID +">");
        }

        boolean isGuildAdmin = hasPermission(guildId, pID, "guild_admin");

        String[] permissions = {
                "kick_permissions", "ban_permissions", "unban_permissions",
                "mute_permissions", "mass_tempRole_permissions", "embed_creator",
                "manage_linked_role", "manage_role_permissions", "create_role",
                "giveaway_access", "guild_admin"
        };


        for (String permission : permissions) {
            boolean hasPerm = isGuildAdmin || hasPermission(guildId, pID, permission);
            String emoji = hasPerm ? ":white_check_mark:" : ":x:";
            eb.addField(permission.replace("_", " "), emoji, true);
        }

        return eb;
    }
}
