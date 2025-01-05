import birthday.birthdayCmds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

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

        // Birthday cmds
        commandData.add(Commands.slash("birthday", "Birthday-related commands")
                .addSubcommands(
                        new SubcommandData("help","Displays help for the birthday command"),
                        new SubcommandData("set","Set your birthday")
                                .addOptions(new birthdayCmds().getBirthday().get(0).setDescription("MM/DD/YYYY")),
                        new SubcommandData("get","Get your birthday")
                            .addOptions(new birthdayCmds().getBirthday().get(1))
                ));

        commandData.add(Commands.slash("birthdayadmin", "Birthday-admin-related commands")
                .addSubcommands(
                        new SubcommandData("set","Set birthday vars")
                                .addOptions(new birthdayCmds().getSetupOptions().get(0))
                                .addOptions(new birthdayCmds().getSetupOptions().get(1))
                                .addOptions(new birthdayCmds().getSetupOptions().get(2)),
                        new SubcommandData("reset","Reset birthday vars")
                ).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );


        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}

