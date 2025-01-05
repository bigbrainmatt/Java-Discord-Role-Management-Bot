package general;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static misc.PermissionManager.deleteCsv;
import static misc.miscInfo.getBotColor;

public class StartAndEnd extends ListenerAdapter {
    @Override
    public void onGuildJoin(GuildJoinEvent event) {

        String owner = event.getGuild().getOwner().getId();

        event.getJDA().getUserById(owner).openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(getBotColor());
            eb.setTitle("Thank you for adding **" + event.getJDA().getSelfUser().getName() + "** to **" + event.getGuild().getName() +"**");
            eb.setDescription("Going on the only way to edit bot permssionw will be using the ``/permission`` command \n Please run the command ``/permissions choices:add triggers:Guild Admin roleoruser:@"+owner+"`` in your server to get started");

            privateChannel.sendMessageEmbeds(eb.build()).complete();
        });
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        deleteCsv(event.getGuild().getId());
    }
}
