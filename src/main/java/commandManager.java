import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}

