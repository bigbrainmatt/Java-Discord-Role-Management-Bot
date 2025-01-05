package modderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class kick extends ListenerAdapter {
    private Member baner;
    private Member bannedMember;
    private String banReason;

    public java.util.List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.USER,"member", "member to ban", true));
        data.add(new OptionData(OptionType.STRING,"reason", "reason for ban", true));
        return data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getInteraction().getName().equalsIgnoreCase("unban")) {
            Member member = event.getOption("member").getAsMember();
            String reason = event.getOption("reason").getAsString();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.red);
            eb.setTitle("Kick member");
            eb.setDescription("Are you sure you would like to kick " + member.getAsMention() + "\n For **" + reason + "**");

            event.replyEmbeds(eb.build())
                    .addActionRow(net.dv8tion.jda.api.interactions.components.buttons.Button.success("unbanCon","Confirm"), Button.danger("unbanDeny", "Cancel")).queue();
            baner = event.getMember();
            bannedMember = member;
            banReason = reason;
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if((baner == event.getMember()) && (event.getComponentId().equalsIgnoreCase("unbanCon"))) {
            event.getInteraction().getMessage().delete().queue();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Member Kicked");
            eb.setColor(Color.green);
            eb.setDescription(bannedMember.getAsMention() + " has been kicked by " + baner + " for **" + banReason + "**");

            event.getGuild().unban(bannedMember.getUser()).complete();

            event.replyEmbeds(eb.build()).queue();
        } if((baner == event.getMember()) && (event.getComponentId().equalsIgnoreCase("unbanDeny"))) {
            event.getInteraction().getMessage().delete().complete();
        }
    }
}
