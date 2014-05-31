package models;

import javax.persistence.*;
import javax.validation.Valid;

import play.db.ebean.Model;
import play.data.validation.Constraints.*;

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

}
