package ch.jamiete.identitycrisis;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import com.google.common.collect.ImmutableList;

public class UUIDUpdater implements Runnable {
    private final IdentityCrisis plugin;

    public UUIDUpdater(IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    // Ignore the excessive thread safety.
    @Override
    public void run() {
        final FileConfiguration c = this.plugin.getConfig();
        ArrayList<String> usernames = new ArrayList<String>();

        for (final String key : c.getConfigurationSection("names").getKeys(false)) {
            this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {

                @Override
                public void run() {
                    c.set("names_temp." + key, c.get("names." + key));
                    c.set("names." + key, null);
                }

            });

            usernames.add(key);
        }

        this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {

            @Override
            public void run() {
                UUIDUpdater.this.plugin.saveConfig();
            }

        });

        UUIDFetcher fetcher = new UUIDFetcher(usernames);
        Map<String, UUID> response = null;
        
        try {
            response = fetcher.call();
        } catch (Exception e) {
            this.plugin.getLogger().severe("An exception occured whilst updating UUIDs.");
            this.plugin.getLogger().severe("This is irrecoverable. Please report it at https://github.com/soaringcats/identitycrisis");
            this.plugin.getLogger().severe("Rename configuration section `names_temp` to `names` and re-run the plugin to attempt again.");
            e.printStackTrace();
        }
        
        for (final Entry<String, UUID> entry : response.entrySet()) {
            this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {

                @Override
                public void run() {
                    c.set("names." + entry.getValue(), c.get("names_temp." + entry.getKey()));
                    UUIDUpdater.this.plugin.getLogger().info("UUID data added for `" + entry.getKey() + "`: `" + entry.getValue() + "` will change to `" + c.get("names_temp." + entry.getKey()));
                }

            });
        }

        this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {

            @Override
            public void run() {
                UUIDUpdater.this.plugin.saveConfig();
                UUIDUpdater.this.plugin.getLogger().info("Upgrade complete.");
            }

        });
    }

    /**
     * @author evilmidget38
     * https://gist.github.com/evilmidget38/df8dcd7855937e9d1e1f
     */
    private class UUIDFetcher implements Callable<Map<String, UUID>> {
        private static final int MAX_SEARCH = 100;
        private static final String PROFILE_URL = "https://api.mojang.com/profiles/page/";
        private static final String AGENT = "minecraft";
        private final JSONParser jsonParser = new JSONParser();
        private final List<String> names;

        public UUIDFetcher(List<String> names) {
            this.names = ImmutableList.copyOf(names);
        }

        @Override
        public Map<String, UUID> call() throws Exception {
            Map<String, UUID> uuidMap = new HashMap<String, UUID>();
            String body = buildBody(names);
            for (int i = 1; i < MAX_SEARCH; i++) {
                HttpURLConnection connection = createConnection(i);
                writeBody(connection, body);
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
                JSONArray array = (JSONArray) jsonObject.get("profiles");
                Number count = (Number) jsonObject.get("size");
                if (count.intValue() == 0) {
                    break;
                }
                for (Object profile : array) {
                    JSONObject jsonProfile = (JSONObject) profile;
                    String id = (String) jsonProfile.get("id");
                    String name = (String) jsonProfile.get("name");
                    UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
                    uuidMap.put(name, uuid);
                }
            }
            return uuidMap;
        }

        private void writeBody(HttpURLConnection connection, String body) throws Exception {
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.write(body.getBytes());
            writer.flush();
            writer.close();
        }

        private HttpURLConnection createConnection(int page) throws Exception {
            URL url = new URL(PROFILE_URL + page);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            return connection;
        }

        @SuppressWarnings("unchecked")
        private String buildBody(List<String> names) {
            List<JSONObject> lookups = new ArrayList<JSONObject>();
            for (String name : names) {
                JSONObject obj = new JSONObject();
                obj.put("name", name);
                obj.put("agent", AGENT);
                lookups.add(obj);
            }
            return JSONValue.toJSONString(lookups);
        }
    }

}
