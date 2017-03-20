package io.discloader.discloader.common.start;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.google.gson.Gson;

import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.client.render.WindowFrame;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.logger.DLErrorStream;
import io.discloader.discloader.common.logger.DLPrintStream;
import io.discloader.discloader.entity.user.UserProfile;

/**
 * DiscLoader client entry point
 * 
 * @author Perry Berman
 * @see DiscLoader
 */
public class Main {
    public static final Gson gson = new Gson();
    public static WindowFrame window;
    public static DiscLoader loader;
    public static boolean usegui = false;
    public static String token;
    private static int shard = 0, shards = 1;
    private static Logger LOGGER;

    public static DiscLoader getLoader() {
        return loader;
    }

    /**
     * @return the lOG
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    public static void main(String... args) throws IOException {
        LOGGER = DiscLoader.LOG;
        System.setOut(new DLPrintStream(System.out, LOGGER));
        System.setErr(new DLErrorStream(System.err, LOGGER));
        System.setProperty("http.agent", "DiscLoader");
        String content = "";
        if (new File("options.json").exists()) {
            Object[] lines = Files.readAllLines(Paths.get("./options.json")).toArray();
            for (Object line : lines)
                content += line;
            options options = gson.fromJson(content, options.class);
            token = options.auth.token;
        }
        parseArgs(args);
        DiscLoader.addEventHandler(new EventListenerAdapter() {
            public void Ready(DiscLoader loader) {
                System.out.printf("Ready as user: %s", loader.user.username);
                // UserProfile profile = loader.user.getProfile().join();

            }
        });

        loader = new DiscLoader(shards, shard);
        loader.login(token);

    }

    public static void parseArgs(String... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("usegui")) {
                usegui = true;
            } else if (args[i].equals("-t")) {
                if (i + 1 < args.length) {
                    token = args[i + 1];
                } else {
                    LOGGER.severe("Expected argument after -t");
                    System.exit(1);
                }
            } else if (args[i].equals("-p")) {
                if (i + 1 < args.length) {
                    CommandHandler.prefix = args[i + 1];
                } else {
                    LOGGER.severe("Expected argument after -p");
                    System.exit(1);
                }
            } else if (args[i].equals("-s")) {
                if (i + 1 < args.length) {
                    String[] g = args[i + 1].split("/");
                    shard = Integer.parseInt(g[0], 10);
                    shards = Integer.parseInt(g[1], 10);
                } else {
                    LOGGER.severe("Expected argument after -s");
                    System.exit(1);
                }
            } else if (args[i].equals("-S")) {
                if (i + 1 < args.length) {
                    shard = -1;
                    shards = Integer.parseInt(args[i + 1], 10);
                } else {
                    LOGGER.severe("Expected argument after -S");
                    System.exit(1);
                }
            }
        }
    }
}
