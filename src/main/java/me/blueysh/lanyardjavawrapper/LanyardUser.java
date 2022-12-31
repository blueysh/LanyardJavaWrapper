package me.blueysh.lanyardjavawrapper;

import org.json.JSONObject;

public record LanyardUser(
        long userId,
        String username,
        String discriminator,
        String status,
        LanyardActivity[] activities,
        Spotify spotify,
        int publicFlags,
        boolean activeOnDiscordWeb,
        boolean activeOnDiscordMobile,
        boolean activeOnDiscordDesktop,
        boolean listeningToSpotify,
        boolean isBot,
        String avatar,
        JSONObject kv
) {

    record Spotify(
        String trackId,
        Lanyard.Timestamps timestamps,
        String song,
        String artist,
        String albumArtUrl,
        String album
    ) {}

}
