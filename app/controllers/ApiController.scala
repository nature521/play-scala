package controllers

import javax.inject._
import play.api._
import play.api.libs.json
import play.api.mvc._
import play.api.libs.functional.syntax._
/**
 * Created by Administrator on 2016/12/21.
 */
@Singleton
class ApiController  @Inject() extends Controller{
  def login = Action {request =>
    var userName : String = request.body.asFormUrlEncoded.get("userName")(0)
    var password : String = request.body.asFormUrlEncoded.get("password")(0)

    //var json = request.body.asJson
    //var userName = (json / "userName").get
    println(userName)
    if(userName.equals("wg") & password.equals("123")){
      Ok("okokok")
    }else{
      Status(500)
    }

  }
  def mainmenu = Action {
    Ok(views.html.mainmenu("aaaa"))
  }
}
