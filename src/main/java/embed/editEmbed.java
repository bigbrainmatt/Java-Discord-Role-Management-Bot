package embed;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.getDenyEmbed;

public class editEmbed extends ListenerAdapter {
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.STRING, "messageid", "Visibility or response", true));
        return data;
    }

    private String chanId;
    private String messageId;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("editembed")) {
            try {
                if (hasPermission(event.getGuild().getId(), event.getUser().getId(), "embed_creator") || hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin")) {
                    messageId = event.getOption("messageid").getAsString();
                    chanId = event.getChannel().getId();


                    TextInput subject = TextInput.create("title", "Title", TextInputStyle.SHORT)
                            .setPlaceholder("Title for embed")
                            .build();

                    TextInput body = TextInput.create("discrption", "Discrption", TextInputStyle.PARAGRAPH)
                            .setPlaceholder("Discription for embed")
                            .build();

                    TextInput color = TextInput.create("color", "Color", TextInputStyle.SHORT)
                            .setPlaceholder("Color for embed in HEX xxxxxx")
                            .build();


                    Modal modal = Modal.create("upEmbed", "Embed")
                            .addComponents(ActionRow.of(subject), ActionRow.of(body), ActionRow.of(color))
                            .build();

                    event.replyModal(modal).queue();
                } else {
                    event.replyEmbeds(getDenyEmbed().build()).queue();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }

        }

    }
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if(event.getModalId().equalsIgnoreCase("upEmbed")){
            String title = event.getValue("title").getAsString();
            String discrption = event.getValue("discrption").getAsString();
            String color = event.getValue("color").getAsString();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(title);
            eb.setDescription(discrption);
            try {
                eb.setColor(HexFormat.fromHexDigits(color));
            } catch (NumberFormatException e) {
                event.reply("Invalid Hex ID").setEphemeral(true).queue();
            }
            event.reply("Embed Updated!").setEphemeral(true).queue();
            event.getChannel().retrieveMessageById(messageId).queue(message ->{
                if (message.getEmbeds().isEmpty()) {
                    event.reply("Please run this in the same channel as the orginal embed").setEphemeral(true).queue();
                    return;
                }
                message.editMessageEmbeds(eb.build()).queue();
            });
        }
    }
}
