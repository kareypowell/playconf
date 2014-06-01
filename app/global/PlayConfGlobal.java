package global;

import play.GlobalSettings;
import play.mvc.Results;
import play.mvc.SimpleResult;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;

/**
 * Created by kareypowell on 5/31/14.
 */
public class PlayConfGlobal extends GlobalSettings {

    @Override
    public Promise<SimpleResult> onHandlerNotFound(RequestHeader request) {
        return Promise.<SimpleResult>pure(Results.notFound(views.html.error.render()));
    }

    @Override
    public Promise<SimpleResult> onError(RequestHeader request, Throwable t) {
        return Promise.<SimpleResult>pure(Results.internalServerError(views.html.error.render()));
    }

}
