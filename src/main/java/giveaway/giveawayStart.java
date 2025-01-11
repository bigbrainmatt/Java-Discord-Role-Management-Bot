package giveaway;

import com.opencsv.exceptions.CsvException;
import misc.miscInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static misc.PermissionManager.hasPermission;
import static misc.miscInfo.getBotColor;
import static misc.miscInfo.getTime;

public class giveawayStart extends ListenerAdapter {

    public static EmbedBuilder giveEmbed;

    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.INTEGER, "endtime", "How long to assign role", true));
        data.add(new OptionData(OptionType.STRING, "endtype", "How long to assign role", true)
                .addChoice("minutes", "minutes")
                .addChoice("hours", "hours")
                .addChoice("days", "days")
                .addChoice("months", "months")
                .addChoice("years", "years")
        );
        data.add(new OptionData(OptionType.INTEGER, "winners", "How many winners for the giveaway", true));
        data.add(new OptionData(OptionType.ROLE, "role", "Role needed", false));
        data.add(new OptionData(OptionType.INTEGER, "temptime", "How long to assign role", false));
        data.add(new OptionData(OptionType.STRING, "temptype", "How long to assign role", false)
                .addChoice("minutes", "minutes")
                .addChoice("hours", "hours")
                .addChoice("days", "days")
                .addChoice("months", "months")
                .addChoice("years", "years")
        );
        return data;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponent().getId().equalsIgnoreCase("giveaway")) {
            MessageEmbed embed = event.getMessage().getEmbeds().get(0);

            MessageEmbed.Field entriesField = null;
            for (MessageEmbed.Field field : embed.getFields()) {
                if (field.getName().equalsIgnoreCase("Entries")) {
                    entriesField = field;
                    break;
                }
            }
            try {
                int currentEntries = Integer.parseInt(entriesField.getValue().replaceAll("[^0-9]", ""));
                int newEntries = currentEntries + 1;

                EmbedBuilder updatedEmbed = new EmbedBuilder(embed);
                updatedEmbed.clearFields();
                for (MessageEmbed.Field field : embed.getFields()) {
                    if (field == entriesField) {
                        updatedEmbed.addField(field.getName(), String.valueOf(newEntries), field.isInline());
                    } else {
                        updatedEmbed.addField(field);
                    }
                }

                event.getMessage().editMessageEmbeds(updatedEmbed.build()).queue();
            } catch (NumberFormatException e) {
            }
        }

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equalsIgnoreCase("giveaway")) {
            try {
                if(hasPermission(event.getGuild().getId(), event.getUser().getId(), "giveaway_access") || hasPermission(event.getGuild().getId(), event.getUser().getId(), "guild_admin")) {
                    boolean temprole = false;
                    int endTime = event.getOption("endtime").getAsInt();
                    String endType = event.getOption("endtime").getAsString();
                    int time = event.getOption("endtime").getAsInt();
                    int winners = event.getOption("winners").getAsInt();

                    Role role = null;
                    int tempTime = 0;
                    String tempType = "";
                    try {
                        role = event.getOption("role").getAsRole();
                        tempTime = event.getOption("temptime").getAsInt();
                        tempType = event.getOption("temptype").getAsString();
                    } catch (Exception e) {
                        temprole = true;
                    }

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Giveaway");
                    eb.setColor(getBotColor());
                    eb.setFooter("Created By: " +event.getUser().getName() + " | " + miscInfo.getTime() , event.getJDA().getSelfUser().getAvatarUrl());

                    eb.addField("Ends", "<t:" + convertToEpochFromNow(getTime(endTime,endType)) + ":R>", false);
                    if(temprole) {
                        eb.addField("Role", role.getAsMention(), false );
                        eb.addField("Temp Role for", "<t:" + convertToEpochFromNow(getTime(tempTime,tempType)) + ":R>", false );
                    }
                    eb.addField("Winners", winners+"", false);
                    eb.addField("Entries", 0+"", false);

                    giveEmbed = eb;
                    event.replyEmbeds(eb.build()).addActionRow(Button.success("giveaway", "Enter Giveaway")).queue();


                    Timer timer = new Timer();

                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                        }
                    };

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            event.reply("Giveaway Done").queue();
                        }
                    }, 0);

                    timer.scheduleAtFixedRate(task, 0, getTime(endTime,endType));

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static long convertToEpochFromNow(long milliseconds) {
        long currentEpochMillis = Instant.now().toEpochMilli();
        long resultEpochMillis = currentEpochMillis + milliseconds;
        return resultEpochMillis / 1000;
    }

    public static int getTime(int endTime, String endType) {
        int allTime = endTime;

        switch (endType) {
            case "minutes":
                allTime = ((endTime * 60)) * 100;
                break;
            case "hours":
                allTime = ((endTime * 60) * 60) * 100;
                break;
            case "days":
                allTime = (((endTime * 60) * 60) * 24) * 100;
                break;
            case "months":
                allTime = ((((endTime * 60) * 60) * 24) * 30) * 100;
                break;
            case "years":
                allTime = (((((endTime * 60) * 60) * 24) * 30) * 12) * 100;
                break;
        }
        return allTime;
    }

}
