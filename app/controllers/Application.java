package controllers;

import actors.EventPublisher;
import actors.messages.CloseConnectionEvent;
import actors.messages.NewConnectionEvent;
import actors.messages.NewProposalEvent;
import actors.messages.UserRegistrationEvent;
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import external.services.OAuthService;
import external.services.TwitterOAuthService;
import play.Play;
import play.libs.F.*;
import play.libs.OAuth.RequestToken;
import play.mvc.*;
import play.data.*;
import models.*;

public class Application extends Controller {

    private static final Form<Proposal> proposalForm = Form.form(Proposal.class);

    private static final OAuthService service = new TwitterOAuthService(
                Play.application().configuration().getString("twitter.consumer.key"),
                Play.application().configuration().getString("twitter.consumer.secret")
            );

    public static Result register() {
        String callbackUrl = routes.Application.registerCallback().absoluteURL(request());
        Tuple<String, RequestToken> t = service.retrieveRequestToken(callbackUrl);
        flash("request_token", t._2.token);
        flash("request_secret", t._2.secret);
        return redirect(t._1);
    }

    public static Result registerCallback() {
        RequestToken token = new RequestToken(flash("request_token"), flash("request_secret"));
        String authVerifier = request().getQueryString("oauth_verifier");
        Promise<JsonNode> userProfile = service.registeredUserProfile(token, authVerifier);

        userProfile.onRedeem(new Callback<JsonNode>() {
            @Override
            public void invoke(JsonNode twitterJson) throws Throwable {
                RegisteredUser user = RegisteredUser.fromJson(twitterJson);
                user.save();
                EventPublisher.ref.tell(new UserRegistrationEvent(user), ActorRef.noSender());
            }
        });

        return redirect(routes.Application.index());
    }

    public static WebSocket<JsonNode> buzz() {
        return new WebSocket<JsonNode>() {
            @Override
            public void onReady(In<JsonNode> in, Out<JsonNode> out) {
                // use the out channel to push data back to the client.
                final String uuid = java.util.UUID.randomUUID().toString();
                EventPublisher.ref.tell(new NewConnectionEvent(uuid, out), ActorRef.noSender());

                in.onClose(new Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        EventPublisher.ref.tell(new CloseConnectionEvent(uuid), ActorRef.noSender());
                    }
                });
            }
        };
    }

    public static Promise<Result> index() {
        Promise<Proposal> keynote = Proposal.findKeynote();
        Promise<Result> result = keynote.map(new Function<Proposal, Result>() {
            @Override
            public Result apply(Proposal keynote) throws Throwable {
                return ok(views.html.index.render(keynote));
            }
        });
        return result;
    }

    public static Result newProposal() {
        return ok(views.html.newProposal.render(proposalForm));
    }

    public static Promise<Result> submitProposal() {
        Form<Proposal> submittedForm = proposalForm.bindFromRequest();

        if (submittedForm.hasErrors()) {
            return Promise.<Result>pure(ok(views.html.newProposal.render(submittedForm)));
        } else {
            final Proposal proposal = submittedForm.get();
            Promise<Result> result = proposal.asyncSave().map(new Function<Void, Result>() {
                @Override
                public Result apply(Void aVoid) throws Throwable {
                    flash("message", "Thanks for submitting your proposal.");
                    EventPublisher.ref.tell(new NewProposalEvent(proposal), ActorRef.noSender());
                    return redirect(routes.Application.index());
                }
            });
            return result;
        }
    }

}
