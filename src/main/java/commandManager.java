import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.List;

public class commandManager extends ListenerAdapter {


    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        // Refreance
        // commandData.add(Commands.slash("example", "example").addOptions(new class().getOptionsAdd().get(0)));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}

