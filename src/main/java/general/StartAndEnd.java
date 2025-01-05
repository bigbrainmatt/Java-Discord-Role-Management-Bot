package general;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.math.BigInteger;
import java.util.EnumSet;

import static misc.PermissionManager.*;
import static misc.miscInfo.getBotColor;

public class StartAndEnd extends ListenerAdapter {
    @Override
    public void onGuildJoin(GuildJoinEvent event) {

        String owner = event.getGuild().getOwner().getId();

        event.getJDA().getUserById(owner).openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(getBotColor());
            eb.setTitle("Thank you for adding **" + event.getJDA().getSelfUser().getName() + "** to **" + event.getGuild().getName() +"**");
            eb.setDescription("Going on the only way to edit bot permssionw will be using the ``/permission`` command \n Please run the command ``/permissions add Guild Admin roleoruser:@"+event.getGuild().getOwner().getEffectiveName()+"`` in your server to get started");

            privateChannel.sendMessageEmbeds(eb.build()).queue();
        });

        TextChannel chan = event.getGuild().createTextChannel(event.getJDA().getSelfUser().getName() +" logs").complete();
        chan.getManager()
                .putPermissionOverride(event.getGuild().getOwner(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                .putPermissionOverride(event.getGuild().getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .complete();

        try {
            addPermission(event.getGuild().getId(),"0","SERVER_LOG_CHAN", chan.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        deleteCsv(event.getGuild().getId());
    }
}
