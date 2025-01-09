package roleAssging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static misc.miscInfo.getTime;

public class tempRole extends ListenerAdapter {

    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.ROLE, "role", "Role needed", true));
        data.add(new OptionData(OptionType.USER, "user", "Person to assign role to", true));
        data.add(new OptionData(OptionType.INTEGER, "time", "How long to assign role", true)
                data.add(new OptionData(OptionType.STRING, "type", "How long to assign role", true)
                .addChoice("hours", "hours")
                .addChoice("days", "days")
                .addChoice("months", "months")
                .addChoice("years", "years")
        );
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("temprole")) {
            Role role = event.getOption("role").getAsRole();
            Member user = event.getOption("user").getAsMember();
            int time = event.getOption("time").getAsInt();
            String type = event.getOption("type").getAsString();

            setTempTime(event,user,role,time,type);

        }
    }

    private void setTempTime(SlashCommandInteractionEvent event, Member user, Role role, int time, String type) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.green);
        eb.setTitle("Temp Role Active");
        eb.setDescription(role.getAsMention() + " has been temporarily assigned to " + user.getAsMention() + " for " + time + " " + type + " by " + event.getUser().getAsMention());
        eb.setFooter(getTime(), event.getJDA().getSelfUser().getAvatarUrl());

        /*
        LocalDateTime twoSecondsLater = LocalDateTime.now().plusSeconds(2);
Date twoSecondsLaterAsDate = Date.from(twoSecondsLater.atZone(ZoneId.systemDefault()).toInstant());

new Timer().schedule(new DatabaseMigrationTask(oldDatabase, newDatabase), twoSecondsLaterAsDate);

         */
    }
}
