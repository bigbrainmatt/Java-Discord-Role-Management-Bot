package roleRelated;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static misc.PermissionManager.ensureCsvExists;
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.getBotColor;
import static misc.miscInfo.getDenyEmbed;
import static roleRelated.listPerms.getPermEmbed;

public class listallperms extends ListenerAdapter {
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.MENTIONABLE, "mention", "Mention to get perms", true));
        data.add(new OptionData(OptionType.BOOLEAN, "hidden", "Visibility or response", true));
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("listallperms")) {
            try {
                if(hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin")) {

                    List<MessageEmbed> embeds = new ArrayList<>();

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(getBotColor());
                    eb.setTitle(event.getGuild().getName() + " permmissions");
                    embeds.add(eb.build());

                    EmbedBuilder ebb;
                    try (CSVReader reader = new CSVReader(new FileReader("guilds/" + event.getGuild().getId() + ".csv"))) {
                        String[] row;
                        int rowIndex = 0;


                        while ((row = reader.readNext()) != null) {
                            rowIndex++;
                            if (rowIndex == 1 || rowIndex == 2 ) {
                                continue;
                            }
                            if (row.length > 1) {
                                ebb = getPermEmbed(event, row[0]);
                                embeds.add(ebb.build());
                            }
                        }

                        if (event.getOption("hidden").getAsBoolean()) {
                            event.replyEmbeds(embeds).setEphemeral(true).queue();
                        } else {
                            event.replyEmbeds(embeds).setEphemeral(false).queue();
                        }

                    } catch (IOException | CsvValidationException e) {
                        e.printStackTrace();
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
