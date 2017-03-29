package io.discloader.discloader.network.rest.actions;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.entity.user.UserProfile;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

/**
 * @author Perry Berman
 *
 */
public class FetchUserProfile {
    private final User user;

    public FetchUserProfile(User user) {
        this.user = user;
    }

    public CompletableFuture<UserProfile> execute() {
        CompletableFuture<UserProfile> future = new CompletableFuture<>();
        user.loader.rest.makeRequest(Endpoints.userProfile(user.getID()), Methods.GET, true).whenCompleteAsync((s, ex) -> {
            if (s != null)
                System.out.println(s);
        });
        return future;
    }
}
