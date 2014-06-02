package global;

import actors.EventPublisher;
import actors.messages.RandomlySelectedTalkEvent;
import akka.actor.ActorRef;
import models.Proposal;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import play.mvc.Results;
import play.mvc.SimpleResult;
import play.libs.F.Promise;
import play.libs.F.Callback;
import play.mvc.Http.RequestHeader;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by kareypowell on 5/31/14.
 */
public class PlayConfGlobal extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        super.onStart(app);
        Akka.system().scheduler().schedule(
                Duration.create(1, TimeUnit.SECONDS),
                Duration.create(10, TimeUnit.SECONDS),
                selectRandomTalks(),
                Akka.system().dispatcher()
        );
    }

    private Runnable selectRandomTalks() {
        return new Runnable() {
            @Override
            public void run() {
                Promise<Proposal> proposal = Proposal.selectRandomTalk();
                proposal.onRedeem(new Callback<Proposal>() {
                    @Override
                    public void invoke(Proposal proposal) throws Throwable {
                        EventPublisher.ref.tell(new RandomlySelectedTalkEvent(proposal), ActorRef.noSender());
                    }
                });
            }
        };
    }

    @Override
    public Promise<SimpleResult> onHandlerNotFound(RequestHeader request) {
        return Promise.<SimpleResult>pure(Results.notFound(views.html.error.render()));
    }

    @Override
    public Promise<SimpleResult> onError(RequestHeader request, Throwable t) {
        return Promise.<SimpleResult>pure(Results.internalServerError(views.html.error.render()));
    }

}
