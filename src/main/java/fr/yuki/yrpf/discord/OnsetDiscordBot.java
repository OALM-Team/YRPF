package fr.yuki.yrpf.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.onfirenetwork.onsetjava.Onset;

import javax.security.auth.login.LoginException;

public class OnsetDiscordBot {
    private final String token;
    private JDA jda;

    public OnsetDiscordBot(String token) {
        this.token = token;
        this.connect();
    }

    private void connect() {
        try {
            this.jda = JDABuilder.createDefault(this.token).build();
            this.jda.awaitReady();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String text) {
        this.jda.getGuilds().get(0).getTextChannelsByName("infos", true).get(0)
                .sendMessage(text).queue();
    }
}
