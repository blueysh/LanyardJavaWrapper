package me.blueysh.lanyardjavawrapper;

public record LanyardActivity(
        int type,
        String state,
        String name,
        String id,
        String details,
        Emoji emoji,
        long createdAt,
        Lanyard.Timestamps timestamps,
        Assets assets,
        String applicationId,
        String syncId,
        String sessionId,
        int flags,
        Party party
) {

    /**
     * The assets of the Activity.
     *
     * @param largeText The text displayed on the large image in the Discord client.
     * @param largeImage The id of the large image displayed in the Discord client.
     * @param smallText The text displayed on the small image in the Discord client.
     * @param smallImage The id of the small image displayed in the Discord client.
     */
    record Assets (
            String largeText,
            String largeImage,
            String smallText,
            String smallImage
    ) {}

    /**
     * The Party data of the activity.
     *
     * @implNote Not fully implemented.
     * @param id The id of the party.
     */
    record Party(
            String id
    ) {}

    /**
     * Represents an Emoji of an activity.
     *
     * @param name The name of the emoji.
     * @param id The id of the emoji, if applicable.
     */
    record Emoji (
            String name,
            String id
    ) {
    }

}