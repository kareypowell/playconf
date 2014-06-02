package models;

import javax.persistence.*;
import javax.validation.Valid;

import org.joda.time.DateTime;
import play.Logger;
import play.db.ebean.Model;
import play.data.validation.Constraints.*;

import play.libs.Akka;
import play.libs.F.*;
import scala.concurrent.ExecutionContext;

import java.sql.Date;

/**
 * Created by kareypowell on 5/31/14.
 */

@Entity
public class Proposal extends Model {

    @Id
    public Long id;

    @Required
    public String title;

    @Required
    @MinLength(value = 10)
    @MaxLength(value = 1000)
    @Column(length = 1000)
    public String content;

    @Required
    public SessionType type = SessionType.OneHourTalk;

    @Required
    public Boolean isApproved = false;

    public String keywords;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    public Speaker speaker;

    public DateTime createdAt = new DateTime();

    public DateTime updatedAt = new DateTime();

    private static Finder<Long, Proposal> find = new Finder<Long, Proposal>(Long.class, Proposal.class);

    private static ExecutionContext ctx = Akka.system().dispatchers().lookup("akka.db-dispatcher");

    public static Promise<Proposal> findKeynote() {
        return Promise.promise(new Function0<Proposal>() {
            @Override
            public Proposal apply() throws Throwable {
                return find.where().eq("type", SessionType.Keynote).findUnique();
            }
        }, ctx).recover(new Function<Throwable, Proposal>() {
            @Override
            public Proposal apply(Throwable throwable) throws Throwable {
                Logger.error("Failed to fetch keynote information", throwable);
                Proposal proposal = new Proposal();
                proposal.title = "COMING SOON!";
                proposal.content = "";
                Speaker speaker = new Speaker();
                speaker.name = "";
                speaker.pictureUrl = "";
                speaker.twitterId = "";
                proposal.speaker = speaker;
                return proposal;
            }
        }, ctx);
    }

    public Promise<Void> asyncSave() {
        return Promise.promise(new Function0<Void>() {
            @Override
            public Void apply() throws Throwable {
                save();
                return null;
            }
        }, ctx);
    }

    public static Promise<Proposal> selectRandomTalk() {
        return Promise.promise(new Function0<Proposal>() {
            @Override
            public Proposal apply() throws Throwable {
                // randomly select one if the first
                Long randomId = (long) (1 + Math.random() * (5 - 1));
                return Proposal.find.byId(randomId);
            }
        } , ctx);
    }

}
