package roleAssging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestRole extends ListenerAdapter {

    HashMap<User, Role> reqU = new HashMap<>();
    private static List<String> AssignableRoles = new ArrayList<>();
    private User reqster;
    private User reqwe;

    public java.util.List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.ROLE, "role", "role needed", true));
        data.add(new OptionData(OptionType.USER, "user", "peron to assign role to", true));
        return data;
    }
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("requestrole")) {
            OptionMapping role = event.getOption("role");
            Role r = role.getAsRole();
            OptionMapping user = event.getOption("user");
            User u = user.getAsUser();
            reqster = event.getUser();
            reqwe = u;

        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("acceptRR") || event.getComponentId().equals("denyRR")) {
            if (event.getMember().getRoles().contains(event.getGuild().getRoleById("1294417080715841537"))
                    || event.getMember().getRoles().contains(event.getGuild().getRoleById("1294417081210769500"))
                    && (event.getUser().equals(reqU.get(reqster)))) {
                if (event.getComponentId().equals("acceptRR")) {
                    event.getMessage().editMessage(" ").setActionRow(Button.primary("1", "Your request has been accpted by: " + event.getUser().getName()).asDisabled()).queue();
                    event.getGuild().addRoleToMember(reqster, reqU.get(reqwe)).complete();
                    event.reply("Done").setEphemeral(true).queue();
                } else if (event.getComponentId().equals("denyRR")) {
                    event.getMessage().editMessage(" ").setActionRow(Button.danger("1", "Your request has been denied by: " + event.getUser().getName()).asDisabled()).queue();
                    event.reply("Done").setEphemeral(true).queue();
                }
            } else {
                event.reply("You can not approve this request").setEphemeral(true).queue();
            }
        }
    }

    public static List<String> convertStringToArrayList(String input) {
        if (input != null && !input.isEmpty()) {
            String[] values = input.split(",");
            for (String value : values) {
                AssignableRoles.add(value.trim());
            }
        }

        return AssignableRoles;
    }
}
