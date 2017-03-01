/**
 * 
 */
package io.discloader.discloader.common;

import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.common.start.Main;

import java.io.IOException;

/**
 * @author Perry Berman
 *
 */
public class Shard {
    public final int shardID;

    public final int shardCount;

    public Process process;

    public Shard(int shardID, int shardCount) {
        this.shardID = shardID;
        this.shardCount = shardCount;
    }

    public void execute() {
        System.out.println(Shard.class.getProtectionDomain().getCodeSource().getLocation().toString());
        try {
            this.process = Runtime.getRuntime().exec(String.format("java -jar DiscLoader.jar -s %d/%d -t %s -p %s", this.shardID, this.shardCount, Main.token, CommandHandler.prefix));
            // this.process.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
