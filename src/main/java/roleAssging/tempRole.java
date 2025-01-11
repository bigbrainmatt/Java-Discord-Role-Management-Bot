package roleAssging;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static misc.PermissionManager.getPermissionValue;
import static misc.miscInfo.getTime;

public class tempRole extends ListenerAdapter {

    private String msgID;

    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.ROLE, "role", "Role needed", true));
        data.add(new OptionData(OptionType.USER, "user", "Person to assign role to", true));
        data.add(new OptionData(OptionType.INTEGER, "time", "How long to assign role", true));
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

            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.green);
            eb.setTitle("Temp Role Active");
            eb.setDescription(role.getAsMention() + " has been temporarily assigned to " + user.getAsMention() + " for " + time + " " + type + " by " + event.getUser().getAsMention());
            eb.setFooter(getTime(), event.getJDA().getSelfUser().getAvatarUrl());

            event.getGuild().addRoleToMember(user,role).complete();

            String chan ="";
            try {
                chan = getPermissionValue(event.getGuild().getId(), "0", "SERVER_LOG_CHAN");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }

            event.getGuild().getTextChannelById(chan).sendMessageEmbeds(eb.build()).queue(message -> {
                msgID = message.getId();
            });

            event.reply(role.getAsMention() + " has been assigned to " + user.getAsMention() + " for " + time + " " + type).setAllowedMentions(Collections.emptyList()).queue();

            String dis = role.getAsMention() + " has been temporarily assigned to " + user.getAsMention() + " for " + time + " " + type + " by " + event.getUser().getAsMention();
            String foot = getTime();

            Timer timer = new Timer();

            String finalChan = chan;
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                }
            };

            timer.scheduleAtFixedRate(task, 0, 1000);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.red);
                    eb.setTitle("Temp Role Inactive");
                    eb.setDescription(dis);
                    eb.setFooter(foot, event.getJDA().getSelfUser().getAvatarUrl());

                    event.getGuild().getTextChannelById(finalChan).sendMessageEmbeds(eb.build()).queue();

                    user.getRoles().remove(role);

                    timer.cancel();
                    System.out.println("Timer stopped.");
                }
            }, 10000);
        }
    }

}
