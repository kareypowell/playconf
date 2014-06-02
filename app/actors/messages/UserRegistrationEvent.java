package actors.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.RegisteredUser;
import play.libs.Json;

/**
 * Created by kareypowell on 6/2/14.
 */
public class UserRegistrationEvent implements UserEvent {

    private RegisteredUser ru;

    public UserRegistrationEvent(RegisteredUser ru) {
        this.ru = ru;
    }

    @Override
    public JsonNode json() {
        final ObjectNode result = Json.newObject();
        result.put("messageType", "registeredUser");
        result.put("name", ru.name);
        result.put("twitterId", ru.twitterId);
        result.put("description", ru.description);
        result.put("pictureUrl", ru.pictureUrl);
        return result;
    }

}
