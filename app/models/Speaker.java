package models;

import javax.persistence.*;

import org.joda.time.DateTime;
import play.db.ebean.Model;
import play.data.validation.Constraints.*;

/**
 * Created by kareypowell on 5/31/14.
 */

@Entity
public class Speaker extends Model {

    @Id
    public Long id;

    @Required
    public String name;

    @Required
    @Email
    public String email;

    @Required
    @MinLength(value = 10)
    @MaxLength(value = 1000)
    @Column(length=1000)
    public String bio;

    @Required
    public String twitterId;

    @Required
    public String pictureUrl;

    public DateTime createdAt = new DateTime();

    public DateTime updatedAt = new DateTime();

}
