package io.discloader.discloader.network.voice;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import io.discloader.discloader.entity.voice.VoiceConnection;

/**
 * @author Perry Berman
 *
 */
public class StreamSchedule extends AudioEventAdapter implements AudioLoadResultHandler {

    public final ArrayList<AudioTrack> tracks;
    private AudioPlayer player;
    protected VoiceConnection connection;

    public StreamSchedule(AudioPlayer player, VoiceConnection connection) {
        this.player = player;

        this.tracks = new ArrayList<>();
        this.connection = connection;
        this.player.addListener(this);
    }

    public void startNext() {
        if (tracks.size() > 0) {
            AudioTrack nextTrack = tracks.get(0);
            player.startTrack(nextTrack, true);
        } else {
            this.connection.getWs().setSpeaking(false);
            connection.provider.close();
        }
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        this.connection.provider.close();
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        this.connection.getWs().setSpeaking(true);
        this.connection.provider.start();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason == AudioTrackEndReason.FINISHED || endReason == AudioTrackEndReason.LOAD_FAILED) {
            tracks.remove(track);
            startNext();
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        this.connection.getWs().setSpeaking(true);
        if (!connection.provider.isOpen()) {
            connection.provider.start();
        }
    }

    @Override
    public void loadFailed(FriendlyException arg0) {
        arg0.printStackTrace();
    }

    @Override
    public void noMatches() {
        System.out.println("Nothing......");
        // oh well, looks like old man jenkins got lost again.
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        for (AudioTrack track : playlist.getTracks()) {
            tracks.add(track);
        }
        startNext();
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        tracks.add(track);
        startNext();
    }

}
