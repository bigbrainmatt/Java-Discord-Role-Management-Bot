package general;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

import static misc.miscInfo.getBotColor;

public class about extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equalsIgnoreCase("about")){
            EmbedBuilder eb = new EmbedBuilder();

            eb.setColor(getBotColor());
            eb.setTitle(event.getJDA().getSelfUser().getName() + " About Menu");
            eb.setDescription(event.getJDA().getSelfUser().getName() + " is a bot designed to show of my skills and could be used to help people that need it ");
            eb.addField("**Version**", "1.0", false);
            eb.addField("**Support**", "DM <@!439958339493953538> for support", false);
            eb.addField("**Guilds**", "" + event.getJDA().getGuilds().size() , false);

            event.replyEmbeds(eb.build()).setEphemeral(true).queue();

        }
    }
}
