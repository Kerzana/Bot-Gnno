package fr.kerzana.botdiscord.command.defaut;

import java.awt.Color;

import fr.kerzana.botdiscord.command.Command;
import fr.kerzana.botdiscord.command.Command.ExecutorType;
import fr.kerzana.botdiscord.command.CommandMap;
import fr.kerzana.botdiscord.command.SimpleCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.UserImpl;

public class HelpCommand {

    private final CommandMap commandMap;

    public HelpCommand(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    @Command(name="help",type=ExecutorType.USER,description="affiche la liste des commandes.")
    private void help(User user, MessageChannel channel, Guild guild){

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Liste des commandes.");
        builder.setColor(Color.CYAN);

        for(SimpleCommand command : commandMap.getCommands()){
            if(command.getExecutorType() == ExecutorType.CONSOLE) continue;

            if(guild != null && command.getPower() > commandMap.getPowerUser(guild, user)) continue;

            builder.addField(command.getName(), command.getDescription(), false);
        }

        if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
        ((UserImpl)user).getPrivateChannel().sendMessage(builder.build()).queue();

        channel.sendMessage(user.getAsMention()+", veuillez regarder vos message privé.").queue();

    }

}