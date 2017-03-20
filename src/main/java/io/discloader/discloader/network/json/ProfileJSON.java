package io.discloader.discloader.network.json;

/**
 * @author Perry Berman
 *
 */
public class ProfileJSON {
    public String premium_since;
    public UserJSON user;
    public ConnectionJSON[] connected_accounts;
    public MutualGuildJSON[] mutual_guilds;
}
