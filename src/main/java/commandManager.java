import embed.editEmbed;
import giveaway.giveawayStart;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import roleAssging.AssignRole;
import roleAssging.tempRole;
import roleAssging.unassignRole;
import roleRelated.*;
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
import java.util.Collections;
import java.util.List;

public class commandManager extends ListenerAdapter {
    public static List data() {
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

        // List all rol permissions
        commandData.add(Commands.slash("listperms", "List out all permissions to a role or user")
                .addOptions(new listPerms().getOptions().get(0))
                .addOptions(new listPerms().getOptions().get(1))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));

        // List all rol permissions
        commandData.add(Commands.slash("listallperms", "List all perms given to any user/role")
                .addOptions(new listPerms().getOptions().get(1))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));

        // List all rol permissions
        commandData.add(Commands.slash("viewusers", "View all members with the selected role")
                .addOptions(new viewUsers().getOptions().get(0))
                .addOptions(new viewUsers().getOptions().get(1))
        );

        // List role info
        commandData.add(Commands.slash("roleinfo", "View infomation of a role")
                .addOptions(new viewUsers().getOptions().get(0))
                .addOptions(new viewUsers().getOptions().get(1))
        );

        // setManager Cmd
        commandData.add(Commands.slash("setmanager", "Set role manager")
                .addOptions(new setManager().getOptions().get(0))
                .addOptions(new setManager().getOptions().get(1))
        );

        // Create Embed Cmd
        commandData.add(Commands.slash("createembed", "Create an embed"));

        // Edit Embed Cmd
        commandData.add(Commands.slash("editembed", "Create an embed")
                .addOptions(new editEmbed().getOptions().get(0))
        );
        // Remove Manger Cmd
        commandData.add(Commands.slash("removemanager", "Create an embed")
                .addOptions(new mangerRemove().getOptions().get(0))
                .addOptions(new mangerRemove().getOptions().get(1))
        );

        // Create role Cmd
        commandData.add(Commands.slash("createrole", "Create an role")
                .addOptions(new createRole().getOptions().get(0))
                .addOptions(new createRole().getOptions().get(1))
                .addOptions(new createRole().getOptions().get(2))
                .addOptions(new createRole().getOptions().get(3))
        );

        // Assign role Cmd
        commandData.add(Commands.slash("assignrole", "Create an role")
                .addOptions(new AssignRole().getOptions().get(0))
                .addOptions(new AssignRole().getOptions().get(1))
        );

        // Unassign role role Cmd
        commandData.add(Commands.slash("unassignrole", "Create an role")
                .addOptions(new unassignRole().getOptions().get(0))
                .addOptions(new unassignRole().getOptions().get(1))
        );

        // Temp role Cmd
        commandData.add(Commands.slash("temprole", "Add a role for a temporary amount of time")
                .addOptions(new tempRole().getOptions().get(0))
                .addOptions(new tempRole().getOptions().get(1))
                .addOptions(new tempRole().getOptions().get(2))
                .addOptions(new tempRole().getOptions().get(3))

        );

        commandData.add(Commands.slash("giveaway", "Start a giveaway")
                .addOptions(new giveawayStart().getOptions().get(0))
                .addOptions(new giveawayStart().getOptions().get(1))
                .addOptions(new giveawayStart().getOptions().get(2))
                .addOptions(new giveawayStart().getOptions().get(3))
                .addOptions(new giveawayStart().getOptions().get(4))
                .addOptions(new giveawayStart().getOptions().get(5))
        );


        return commandData;
    }


    /*@Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(data()).queue();
    }

     */

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        event.getGuild().updateCommands().addCommands(data()).queue();
    }
}

