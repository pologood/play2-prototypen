package controllers.common;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Common Application"));
    }
  
}
