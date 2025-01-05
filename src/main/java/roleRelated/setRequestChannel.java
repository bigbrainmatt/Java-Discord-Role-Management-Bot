package roleRelated;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static misc.PermissionManager.*;
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.*;

public class setRequestChannel extends ListenerAdapter {
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.CHANNEL,"channel", "Channel to set for role requests", true));
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("setrequestchannel")) {
            try {
                if (hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin")) {
                    TextChannel chan = event.getOption("channel").getAsChannel().asTextChannel();
                    try {
                        addPermission(event.getGuild().getId(), "0", "ROLE_REC_CHAN", chan.getId());
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Channel Set");
                        eb.setColor(getBotColor());
                        eb.setDescription("The role request channel has been set to " + chan.getAsMention());

                        event.replyEmbeds(eb.build()).setEphemeral(true).queue();

                        eb.setDescription(event.getUser().getAsMention() + " has set the role request channel to " + chan.getAsMention());

                        eb.setFooter(getTime(), event.getJDA().getSelfUser().getAvatarUrl());
                        String LogChan = null;

                        try {
                            LogChan = getPermissionValue(event.getGuild().getId(), "0", "SERVER_LOG_CHAN");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (CsvException e) {
                            throw new RuntimeException(e);
                        }

                        event.getGuild().getTextChannelById(LogChan).sendMessageEmbeds(eb.build()).queue();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (CsvException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    event.replyEmbeds(getDenyEmbed().build()).setEphemeral(true).queue();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        }
        }
}
