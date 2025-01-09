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

            setTempTime(event,user,role,time,type);

        }
    }

    private void setTempTime(SlashCommandInteractionEvent event, Member user, Role role, int time, String type) {
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

        String[] messageId = {""};
        event.getGuild().getTextChannelById(chan).sendMessageEmbeds(eb.build()).queue(message -> {
            messageId[0] = message.getId();
            System.out.println(messageId[0]);
            System.out.println("Done");
        });
        String mID = messageId[0];

        event.reply(role.getAsMention() + " has been assigned to " + user.getAsMention() + " for " + time + " " + type).setAllowedMentions(Collections.emptyList()).queue();

        String dis = role.getAsMention() + " has been temporarily assigned to " + user.getAsMention() + " for " + time + " " + type + " by " + event.getUser().getAsMention();
        String foot = getTime();

        LocalDateTime twoSecondsLater = LocalDateTime.now().plusSeconds(2);
        Date twoSecondsLaterAsDate = Date.from(twoSecondsLater.atZone(ZoneId.systemDefault()).toInstant());
        new Timer().schedule(deactivate(role, user, event, chan, mID, dis, foot), twoSecondsLaterAsDate);


    }

    private TimerTask deactivate(Role role, Member user, SlashCommandInteractionEvent event, String chan, String mID, String dis, String foot) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.red);
        eb.setTitle("Temp Role Inactive");
        eb.setDescription(dis);
        eb.setFooter(foot, event.getJDA().getSelfUser().getAvatarUrl());

        event.getGuild().getTextChannelById(chan).getHistory().getMessageById(mID).editMessageEmbeds(eb.build()).queue();

        user.getRoles().remove(role);
        return null;
    }


}
