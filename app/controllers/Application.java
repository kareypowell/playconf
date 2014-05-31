package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import models.*;

public class Application extends Controller {

    private static final Form<Proposal> proposalForm = Form.form(Proposal.class);

    public static Result index() {
        return ok(index.render("Hello Karey, your application is ready."));
    }

    public static Result welcome(String name) {
        return ok("<h1>Welcome, " + name + "</h1>").as("text/html");
    }

    public static Result newProposal() {
        return ok(views.html.newProposal.render(proposalForm));
    }

    public static Result submitProposal() {
        Form<Proposal> submittedForm = proposalForm.bindFromRequest();

        if (submittedForm.hasErrors()) {
            return ok(views.html.newProposal.render(submittedForm));
        } else {
            Proposal proposal = submittedForm.get();
            proposal.save();
            flash("message", "Thanks for submitting your proposal.");
            return redirect(routes.Application.index());
        }
    }

}
