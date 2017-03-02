/**
 * 
 */
package io.discloader.discloader.network.voice;

import io.discloader.discloader.entity.voice.VoiceConnection;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/**
 * @author Perry Berman
 *
 */
public class StreamSchedule extends AudioEventAdapter implements AudioLoadResultHandler {

    public ArrayList<AudioTrack> tracks;
    private AudioPlayer player;
    protected VoiceConnection connection;

    public StreamSchedule(AudioPlayer player, VoiceConnection connection) {
        this.player = player;
        
        this.tracks = new ArrayList<>();
        this.connection = connection;
        this.player.addListener(this);
        
//        player.
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        this.connection.provider.close();
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        this.connection.provider.start();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
      // A track started playing
        System.out.println("TrackStart");
        this.connection.getWs().setSpeaking(true);
        this.connection.provider.start();
    }
    
    @Override
    public void loadFailed(FriendlyException arg0) {
        arg0.printStackTrace();
    }

    @Override
    public void noMatches() {
        System.out.println("Failed to load requested track");
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        for (AudioTrack track : playlist.getTracks()) {
            tracks.add(track);
        }
        player.startTrack(tracks.get(0),  true);
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        System.out.println(track.getInfo().title);
        tracks.add(track);
        player.startTrack(tracks.get(0),  true);
    }

}
