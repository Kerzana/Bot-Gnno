package fr.kerzana.botdiscord;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import fr.kerzana.botdiscord.command.CommandMap;
import fr.kerzana.botdiscord.event.BotListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class BotDiscord implements Runnable{

    private final JDA jda;
    private final CommandMap commandMap = new CommandMap(this);
    private final Scanner scanner = new Scanner(System.in);

    private boolean running;

    public BotDiscord() throws LoginException, IllegalArgumentException, RateLimitedException {
        jda = new JDABuilder(AccountType.BOT).setToken("MzgwMDkwNTMxNTg3MjkzMTg1.DO-FCA.vaERN0m1HZAEtteYviVXW_Dpdk8").buildAsync();
        jda.addEventListener(new BotListener(commandMap));
        System.out.println("Bot connected.");
    }

    public JDA getJda() {
        return jda;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            if(scanner.hasNextLine()) commandMap.commandConsole(scanner.nextLine());
        }

        scanner.close();
        System.out.println("Bot stopped.");
        jda.shutdown();
        commandMap.save();
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            BotDiscord botDiscord = new BotDiscord();
            new Thread(botDiscord, "bot").start();
        } catch (LoginException | IllegalArgumentException | RateLimitedException e) {
            e.printStackTrace();
        }
    }
}