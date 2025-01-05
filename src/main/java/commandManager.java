import roleRelated.copyPerms;
import roleRelated.permissions;
import modderation.ban;
import modderation.kick;
import modderation.unban;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import roleRelated.setRequestChannel;

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

        // Kick Cmd
        commandData.add(Commands.slash("kick", "Kick a member form the server")
                .addOptions(new kick().getOptions().get(0))
                .addOptions(new kick().getOptions().get(1)));

        // Ban Cmd
        commandData.add(Commands.slash("ban", "Ban a member form the server")
                .addOptions(new ban().getOptions().get(0))
                .addOptions(new ban().getOptions().get(1)));

        // Ban Cmd
        commandData.add(Commands.slash("unban", "Unban a member form the server")
                .addOptions(new unban().getOptions().get(0))
                .addOptions(new unban().getOptions().get(1)));

        // Permissions Cmd
        commandData.add(Commands.slash("permissions", "Set permissions")
                .addOptions(new permissions().getOptions().get(0))
                .addOptions(new permissions().getOptions().get(1))
                .addOptions(new permissions().getOptions().get(2))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));

        // Ban Cmd
        commandData.add(Commands.slash("copyperms", "Copy a roles perms from one onto another")
                .addOptions(new copyPerms().getOptions().get(0))
                .addOptions(new copyPerms().getOptions().get(1))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));

        // Role request chan Cmd
        commandData.add(Commands.slash("setrequestchannel", "Set the channel that the role request will be sent to")
                .addOptions(new setRequestChannel().getOptions().get(0))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));


        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}

