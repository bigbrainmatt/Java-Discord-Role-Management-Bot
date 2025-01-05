import general.permissions;
import modderation.ban;
import modderation.kick;
import modderation.unban;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;

public class commandManager extends ListenerAdapter {


    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        // Refreance
        // commandData.add(Commands.slash("example", "example").addOptions(new class().getOptionsAdd().get(0)));

        // -------------- General Cmds -----------------
        // About Cmd
        commandData.add(Commands.slash("about", "Some more info about the bot"));

        // Ban Cmd
        commandData.add(Commands.slash("ban", "Ban a user")
                .addOptions(new ban().getOptions().get(0)).addOptions(new ban().getOptions().get(1))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)));

        // Unban Cmd
        commandData.add(Commands.slash("unban", "Unban a user")
                .addOptions(new unban().getOptions().get(0)).addOptions(new unban().getOptions().get(1))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)));

        // Kick Cmd
        commandData.add(Commands.slash("kick", "Kick a user")
                .addOptions(new kick().getOptions().get(0)).addOptions(new kick().getOptions().get(1))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.KICK_MEMBERS)));

        // Permissions Cmd
        commandData.add(Commands.slash("permissions", "Set permissions")
                .addOptions(new permissions().getOptions().get(0))
                .addOptions(new permissions().getOptions().get(1))
                .addOptions(new permissions().getOptions().get(2))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));


        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}

