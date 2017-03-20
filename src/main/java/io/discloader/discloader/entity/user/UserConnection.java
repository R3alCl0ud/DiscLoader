package io.discloader.discloader.entity.user;

import io.discloader.discloader.network.json.ConnectionJSON;

/**
 * @author Perry Berman
 *
 */
public class UserConnection {
    private String type;
    private String name;
    private String id;

    public UserConnection(ConnectionJSON data) {
        type = data.type;
        name = data.name;
        id = data.id;
    }

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }
}
