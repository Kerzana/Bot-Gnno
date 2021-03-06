package fr.kerzana.botdiscord.music;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import fr.kerzana.botdiscord.music.MusicCommand;

public class AudioListener extends AudioEventAdapter{

    private final BlockingQueue<AudioTrack> tracks = new LinkedBlockingQueue<>();
    private final MusicPlayer player;

    public AudioListener(MusicPlayer player){
        this.player = player;
    }

    public BlockingQueue<AudioTrack> getTracks() {
        return tracks;
    }

    public int getTrackSize(){
        return tracks.size();
    }

    public void nextTrack(){
        if(tracks.isEmpty()){
            if(player.getGuild().getAudioManager().getConnectedChannel() != null) {
                player.getGuild().getAudioManager().closeAudioConnection();
            }
            return;
        }
        player.getAudioPlayer().startTrack(tracks.poll(), false);
    }

    public void kipTrack() {
        if(tracks.isEmpty()){
            if(player.getGuild().getAudioManager().getConnectedChannel() != null) {
                player.getGuild().getAudioManager().getConnectTimeout();
            }
            return;
        }
        player.getAudioPlayer().startTrack(tracks.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) kipTrack();
    }

    public void queue(AudioTrack track) {
        if (!player.getAudioPlayer().startTrack(track, true)) tracks.offer(track);
    }

}