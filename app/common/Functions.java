package common;

/**
 * Created by kareypowell on 6/1/14.
 */

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.libs.F.Function;
import play.libs.Json;
import play.libs.WS.Response;
import play.mvc.Result;
import play.mvc.Results;

public class Functions {
    public static Function<Response, JsonNode> responseToJson = new Function<Response, JsonNode>() {
        public JsonNode apply(Response response) {
            return response.asJson();
        }
    };

    public static Function<JsonNode, Result> jsonToResult = new Function<JsonNode, Result>() {
        public Result apply(JsonNode node) {
            return Results.ok(node);
        }
    };

    public static Function<Throwable, JsonNode> fetchUserError = new Function<Throwable, JsonNode>() {
        @Override
        public JsonNode apply(Throwable throwable) throws Throwable {
            Logger.error("Failed to fetch profile", throwable);
            return Json
                    .parse("{\"error\": \"failed to fetch the profile of user\"}");
        }
    };

    public static Function<JsonNode, String> findTextElement(final String path) {
        return new Function<JsonNode, String>() {
            public String apply(JsonNode node) {
                return node.findPath(path).asText();
            }
        };
    }

}