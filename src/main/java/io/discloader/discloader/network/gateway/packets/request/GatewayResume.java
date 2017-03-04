package io.discloader.discloader.network.gateway.packets.request;

public class GatewayResume {
    public String session_id;
    public String token;
    public int seq;
    
    public GatewayResume(String sessionID, String token, int seq) {
        this.session_id = sessionID;
        this.token = token;
        this.seq = seq;
    }
}
