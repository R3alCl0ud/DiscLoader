package io.discloader.discloader.network.gateway;

public enum OPCodes {
    DISPATCH, HEARTBEAT, IDENTIFY, STATUS_UPDATE, VOICE_STATE_UPDATE, VOICE_SERVER_PING, RESUME, RECONNECT, REQUEST_GUILD_MEMEBERS, INVALID_SESSION, HELLO, HEARTBEAT_ACK, GUILD_SYNC;
    
    
    public String toString() {
        return "" + ordinal();
    }
}
