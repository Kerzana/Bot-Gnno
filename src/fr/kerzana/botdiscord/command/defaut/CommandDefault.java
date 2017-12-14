package fr.kerzana.botdiscord.command.defaut;

import fr.kerzana.botdiscord.BotDiscord;
import fr.kerzana.botdiscord.command.Command;
import fr.kerzana.botdiscord.command.Command.ExecutorType;
import fr.kerzana.botdiscord.command.CommandMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.impl.MemberImpl;
import net.dv8tion.jda.core.entities.impl.UserImpl;

import java.awt.*;
import java.util.List;

public class CommandDefault {

    private final BotDiscord botDiscord;
    private final CommandMap commandMap;

    public CommandDefault(BotDiscord botDiscord, CommandMap commandMap){
        this.botDiscord = botDiscord;
        this.commandMap = commandMap;
    }

    @Command(name="stop",type=ExecutorType.CONSOLE)
    private void stop(){
        botDiscord.setRunning(false);
    }

    @Command(name="power",power=150, description="Set le Niveaux de Perm du joueur !")
    private void power(User user, MessageChannel channel, Message message, String[] args)
    {
        if(args.length == 0 || message.getMentionedUsers().size() == 0)
        {
            if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
            ((UserImpl)user).getPrivateChannel().sendMessage("power <Power> <@User>").queue();
            return;
        }

        int power = 0;
        try{
            power = Integer.parseInt(args[0]);
        }catch(NumberFormatException nfe){
            if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
            ((UserImpl)user).getPrivateChannel().sendMessage("Le power doit être un nombre.").queue();
            return;
        }

        User target = message.getMentionedUsers().get(0);
        commandMap.addUserPower(target, power);

        if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
        ((UserImpl)user).getPrivateChannel().sendMessage("Le power de "+target.getAsMention()+" est maintenant de "+power).queue();
    }

    @Command(name="info",type=ExecutorType.USER, description="Donne des info sur la Personne.")
    private void info(User user, MessageChannel channel){
        if(channel instanceof TextChannel){
            TextChannel textChannel = (TextChannel)channel;
            if(!textChannel.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) return;
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(user.getName(), null, user.getAvatarUrl()+"?size=256");
        builder.setTitle("Informations");
        builder.setDescription("[>](1) le message a été envoyé depuis le channel "+channel.getName());
        builder.setColor(Color.green);
        builder.addField(user.getName(), user.getId(),true);

        channel.sendMessage(builder.build()).queue();
    }
}