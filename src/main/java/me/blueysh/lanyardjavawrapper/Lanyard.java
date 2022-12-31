package me.blueysh.lanyardjavawrapper;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * The wrapper for the Lanyard API.
 * <p>
 * <a href="https://github.com/phineas/lanyard">Visit the GitHub page for the Lanyard API here.</a>
 */
public class Lanyard {

    /**
     * Finds a user's information from the Lanyard API.
     *
     * @param userId The ID of the Discord User to find.
     * @return A {@code LanyardUser} object upon success, otherwise {@code null}.
     * @throws LanyardException Thrown if an error is encountered finding the user.
     */
    public static LanyardUser getLanyardUser(long userId) throws LanyardException {
        try {

            URL url = new URL("https://api.lanyard.rest/v1/users/" + userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject baseJSON = new JSONObject(response.toString());

            if (!baseJSON.getBoolean("success")) {
                throw new LanyardException(baseJSON.getJSONObject("error").getString("message") + " (code: " + baseJSON.getJSONObject("error").getString("code"));
            }

            JSONObject dataJSON = baseJSON.getJSONObject("data");
            JSONObject userJSON = dataJSON.getJSONObject("discord_user");

            ArrayList<LanyardActivity> activities = new ArrayList<>();

            dataJSON.getJSONArray("activities").forEach((o -> {
                JSONObject activityObj = (JSONObject) o;
                LanyardActivity activity = new LanyardActivity(

                        activityObj.has("type") ? activityObj.getInt("type") : 0,
                        activityObj.has("state") ? activityObj.getString("state") : null,
                        activityObj.has("name") ? activityObj.getString("name") : null,
                        activityObj.has("id") ? activityObj.getString("id") : null,
                        activityObj.has("details") ? activityObj.getString("details") : null,
                        activityObj.has("emoji") ? new LanyardActivity.Emoji(
                                activityObj.getJSONObject("emoji").has("name") ? activityObj.getJSONObject("emoji").getString("name") : null,
                                activityObj.getJSONObject("emoji").has("id") ? activityObj.getJSONObject("emoji").getString("id") : null
                        ) : null,
                        activityObj.has("created_at") ? activityObj.getLong("created_at") : 0L,
                        activityObj.has("timestamps") ? new Timestamps(
                                activityObj.getJSONObject("timestamps").has("start") ? activityObj.getJSONObject("timestamps").getLong("start") : 0L,
                                activityObj.getJSONObject("timestamps").has("end") ? activityObj.getJSONObject("timestamps").getLong("end") : 0L
                        ) : null,
                        activityObj.has("assets") ? new LanyardActivity.Assets(
                                activityObj.getJSONObject("assets").has("large_text") ? activityObj.getJSONObject("assets").getString("large_text") : null,
                                activityObj.getJSONObject("assets").has("large_image") ? activityObj.getJSONObject("assets").getString("large_image") : null,
                                activityObj.getJSONObject("assets").has("small_text") ? activityObj.getJSONObject("assets").getString("small_text") : null,
                                activityObj.getJSONObject("assets").has("small_image") ? activityObj.getJSONObject("assets").getString("small_image") : null
                        ) : null,
                        activityObj.has("application_id") ? activityObj.getString("application_id") : null,
                        activityObj.has("sync_id") ? activityObj.getString("sync_id") : null,
                        activityObj.has("session_id") ? activityObj.getString("session_id") : null,
                        activityObj.has("flags") ? activityObj.getInt("flags") : 0,
                        activityObj.has("party") ? new LanyardActivity.Party(
                                activityObj.getJSONObject("party").has("id") ? activityObj.getJSONObject("party").getString("id") : null
                        ) : null

                );
                activities.add(activity);
            }));

            return new LanyardUser(
                    userId,
                    userJSON.has("username") ? userJSON.getString("username") : null,
                    userJSON.has("discriminator") ? userJSON.getString("discriminator") : null,
                    dataJSON.has("discord_status") ? dataJSON.getString("discord_status") : null,
                    activities.toArray(new LanyardActivity[0]),
                    (dataJSON.has("spotify") && !dataJSON.isNull("spotify")) ? new LanyardUser.Spotify(
                            dataJSON.getJSONObject("spotify").has("track_id") ? dataJSON.getJSONObject("spotify").getString("track_id") : null,
                            dataJSON.getJSONObject("spotify").has("timestamps") ? new Timestamps(
                                    dataJSON.getJSONObject("spotify").getJSONObject("timestamps").has("start") ? dataJSON.getJSONObject("spotify").getJSONObject("timestamps").getLong("start") : 0L,
                                    dataJSON.getJSONObject("spotify").getJSONObject("timestamps").has("end") ? dataJSON.getJSONObject("spotify").getJSONObject("timestamps").getLong("end") : 0L
                            ) : null,
                            dataJSON.getJSONObject("spotify").has("song") ? dataJSON.getJSONObject("spotify").getString("song") : null,
                            dataJSON.getJSONObject("spotify").has("artist") ? dataJSON.getJSONObject("spotify").getString("artist") : null,
                            dataJSON.getJSONObject("spotify").has("album_art_url") ? dataJSON.getJSONObject("spotify").getString("album_art_url") : null,
                            dataJSON.getJSONObject("spotify").has("album") ? dataJSON.getJSONObject("spotify").getString("album") : null
                    ) : null,
                    userJSON.has("public_flags") ? userJSON.getInt("public_flags") : 0,
                    dataJSON.getBoolean("active_on_discord_web"),
                    dataJSON.getBoolean("active_on_discord_mobile"),
                    dataJSON.getBoolean("active_on_discord_desktop"),
                    dataJSON.getBoolean("listening_to_spotify"),
                    userJSON.getBoolean("bot"),
                    userJSON.has("avatar") ? userJSON.getString("avatar") : null,
                    dataJSON.has("kv") ? dataJSON.getJSONObject("kv") : null

            );
        }catch (IOException ioEx) {
            throw new LanyardException("An unexpected internal error occurred. Report this at https://github.com/blueysh/lanyardjavawrapper/issues:\n" + ioEx.getClass().getName() + ": " + ioEx.getMessage());
        }
    }

    /**
     * Represents a set of timestamps for an object, which may be present in Activities or Spotify data.
     *
     * @param start The start time of the activity
     * @param end The end time of the activity
     */
    record Timestamps (
            long start,
            long end
    ) {}
}
