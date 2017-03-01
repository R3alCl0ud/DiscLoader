/**
 * 
 */
package io.discloader.discloader.common;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Perry Berman
 *
 */
public class ShardManager {

    public HashMap<Integer, Shard> shards;
    
    public int shardCount;
    public int launchedShards = 0;
    
    public ShardManager(int shardCount) {
        this.shardCount = shardCount;
        this.shards = new HashMap<Integer, Shard>();
        
        this.createShards(0);
    }
    
    public void createShards(final int id) {
        if (launchedShards == this.shardCount) {
            return;
        }
        Shard shard = new Shard(id, shardCount);
        this.shards.put(id, shard);
        shard.execute();
        launchedShards++;
        TimerTask launch = new TimerTask() {

            @Override
            public void run() {
                createShards(id + 1);
            }
        };
        
        new Timer().schedule(launch, 5000);
    }
    
}
