package roleAssging;

import com.opencsv.exceptions.CsvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
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
import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.getTime;
import static misc.miscInfo.isRoleHigher;

public class tempRole extends ListenerAdapter {

    private static String msgID;

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
        if (event.getName().equals("temprole")) {
            int time = event.getOption("time").getAsInt();
            tempRoleExport(event, time);
        }
    }

    public static void tempRoleExport(SlashCommandInteractionEvent event, int time) {

        if (event.getName().equals("temprole")) {
            Role role = event.getOption("role").getAsRole();
            Member user = event.getOption("user").getAsMember();
            String type = event.getOption("type").getAsString();

            if (!isRoleHigher(event.getMember(), role)) {
                throw new HierarchyException("");
            }

            try {
                if (hasPermission(event.getGuild().getId(), event.getUser().getId(), "mass_tempRole_permissions") || hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin")) {
                    if (!user.getRoles().contains(role)) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(Color.green);
                        eb.setTitle("Temp Role Active");
                        eb.setDescription(role.getAsMention() + " has been temporarily assigned to " + user.getAsMention() + " for " + time + " " + type + " by " + event.getUser().getAsMention());
                        eb.setFooter(getTime(), event.getJDA().getSelfUser().getAvatarUrl());

                        event.getGuild().addRoleToMember(user, role).complete();

                        String chan = "";
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

                        event.reply(role.getAsMention() + " has been assigned to " + user.getAsMention() + " for **" + time + " " + type + "**").setAllowedMentions(Collections.emptyList()).queue();

                        String dis = role.getAsMention() + " has been temporarily assigned to " + user.getAsMention() + " for " + time + " " + type + " by " + event.getUser().getAsMention();
                        String foot = getTime();

                        Timer timer = new Timer();

                        String finalChan = chan;
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {

                            }
                        };

                        int allTime = time;

                        switch (type) {
                            case "hours":
                                allTime = ((time * 60) * 60) * 100;
                                break;
                            case "days":
                                allTime = (((time * 60) * 60) * 24) * 100;
                                break;
                            case "months":
                                allTime = ((((time * 60) * 60) * 24) * 30) * 100;
                                break;
                            case "years":
                                allTime = (((((time * 60) * 60) * 24) * 30) * 12) * 100;
                                break;
                        }

                        timer.scheduleAtFixedRate(task, 0, allTime);

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {

                                EmbedBuilder eb = new EmbedBuilder();
                                eb.setColor(Color.red);
                                eb.setTitle("Temp Role Inactive");
                                eb.setDescription(dis);
                                eb.setFooter(foot, event.getJDA().getSelfUser().getAvatarUrl());

                                event.getGuild().getTextChannelById(finalChan).sendMessageEmbeds(eb.build()).queue();

                                event.getGuild().removeRoleFromMember(user, role).complete();

                                timer.cancel();
                            }
                        }, 0);
                    } else {
                        event.reply("User already has this role").setEphemeral(true).queue();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            } catch (HierarchyException e) {
                event.reply("Can't modify a role with higher or equal highest role than yourself!").setEphemeral(true).queue();
            }

        }

    }
}
