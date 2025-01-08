package roleAssging;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static misc.ManagerManager.getManagerForRole;
import static misc.miscInfo.*;

public class AssignRole extends ListenerAdapter {

    private static List<String> AssignableRoles = new ArrayList<>();
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.ROLE, "role", "Role needed", true));
        data.add(new OptionData(OptionType.USER, "user", "Person to assign role to", true));
        return data;
    }
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("assignrole")) {
            Role role = event.getOption("role").getAsRole();
            Member user = event.getOption("user").getAsMember();

            String roleMangSlot1 = "";
            try {
                roleMangSlot1 = getManagerForRole(event.getGuild().getId(),role.getId(), 1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }

            String roleMangSlot2 = "";
            try {
                roleMangSlot2 = getManagerForRole(event.getGuild().getId(),role.getId(), 2);
                if(roleMangSlot2 == "") {
                    roleMangSlot2 = "0";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }

            if(roleMangSlot1.equals(event.getUser().getId()) || event.getMember().getRoles().contains(event.getGuild().getRoleById(roleMangSlot2))) {
                if(isRoleHigher(event.getMember(), role)) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(getBotColor());
                    eb.setTitle("Cant assign this role");
                    eb.setDescription("You can not assign a role higher then your highest role");
                    event.replyEmbeds(eb.build()).queue();
                } else {
                    if(!user.getRoles().contains(role)) {
                        event.getGuild().addRoleToMember(user, role).complete();
                        event.reply("I have added " + role.getAsMention() + " to " + user.getAsMention()).setAllowedMentions(Collections.emptyList()).queue();
                        embedLog(event, false, "Assigned Role", event.getUser().getAsMention() + " has assigned the role " + role.getAsMention() + " to " + user.getAsMention());
                    } else {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(getBotColor());
                        eb.setTitle("Cant assign this role");
                        eb.setDescription("The user already has this role");
                        event.replyEmbeds(eb.build()).queue();
                    }
                }
            } else {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(getBotColor());
                eb.setTitle("Cant assign this role");
                eb.setDescription("You are not a manager of this role you can not assign it to to other players");
                event.replyEmbeds(eb.build()).queue();
                embedLog(event,true,"Assigned Role", event.getUser().getAsMention() + " has attempted to assign the role " + role.getAsMention() + " to " +user.getAsMention() );
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
