package general;

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
import static misc.miscInfo.*;


public class permissions extends ListenerAdapter {
    public java.util.List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.STRING, "choices", "Remove or add", true)
                .addChoice("remove", "Remove")
                .addChoice("add", "Add")
        );

        data.add(new OptionData(OptionType.STRING, "triggers", "reason for ban", true)
                .addChoice("Kick Permissions", "kick_permissions")
                .addChoice("Ban Permissions", "ban_permissions")
                .addChoice("Unban Permissions", "unban_permissions")
                .addChoice("Mute Permissions", "mute_permissions")
                .addChoice("Mass TempRole Permissions", "mass_tempRole_permissions")
                .addChoice("Embed Creator", "embed_creator")
                .addChoice("Manage Linked Role", "manage_linked_role")
                .addChoice("Manage Role Permissions", "manage_role_permissions")
                .addChoice("Create Role", "create_role")
                .addChoice("Create Role", "giveaway_access")
                .addChoice("Guild Admin", "guild_admin")
        );

        data.add(new OptionData(OptionType.MENTIONABLE, "roleoruser", "Role or User", true));

        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("permissions")) {

            String guildId = event.getGuild().getId();
            String pID = null;

            String perms = event.getOption("triggers").getAsString();

            try {
                pID = event.getOption("roleoruser").getAsUser().getId();
            } catch (Exception e) {
                try {
                    pID = event.getOption("roleoruser").getAsRole().getId();
                } catch (Exception x) {

                }
            }

            try {
                ensureCsvExists(guildId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (hasPermission(guildId, event.getUser().getId(), "guild_admin") || (event.getGuild().getOwner().getId().equals(event.getUser().getId()))) {
                    if (event.getOption("choices").getAsString().equalsIgnoreCase("Remove")) {
                        try {
                            if (!rowExistsById(guildId, pID)) {
                                EmbedBuilder eb = new EmbedBuilder();
                                eb.setColor(getBotColor());
                                eb.setTitle("Add Permssions First");
                                eb.setDescription("You need to add a permssion to a user / role before you can try to remove one");

                                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                            } else {
                                removePermission(guildId, pID, perms);

                                EmbedBuilder eb = new EmbedBuilder();
                                eb.setColor(getBotColor());
                                eb.setTitle("Removed Permission");
                                eb.setDescription("You have removed the permission **" + perms + "** from the role/user **" + pID + "**");

                                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (CsvException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (event.getOption("choices").getAsString().equalsIgnoreCase("add")) {
                        try {
                            addPermission(guildId, pID, perms, "1");

                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setColor(getBotColor());
                            eb.setTitle("Added Permission");
                            eb.setDescription("You have added the permission **" + perms + "** to the role/user **" + pID + "**");

                            event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (CsvException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if (!hasPermission(guildId, event.getUser().getId(), "guild_admin")) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(getBotColor());
                    eb.setTitle("No Permissions");
                    eb.setDescription("You must be a **Guild Admin** to assign other members permissions");

                    event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
