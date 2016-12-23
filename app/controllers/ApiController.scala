package controllers

import javax.inject._

import anorm.SqlQueryResult
import models.{User, UserService}
import play.api._
import play.api.libs.json
import play.api.mvc._
import play.api.libs.functional.syntax._
/**
 * Created by Administrator on 2016/12/21.
 */
@Singleton
class ApiController  @Inject() (userService: UserService)extends Controller{
  def login = Action {request =>
    var userName : String = request.body.asFormUrlEncoded.get("userName")(0)
    var password : String = request.body.asFormUrlEncoded.get("password")(0)
    //var json = request.body.asJson
    //var userName = (json / "userName").get
    if(userService.findUser(userName, password) > 0){
      Ok("okokok")
    }else{
      Status(500)
    }
  }
  def mainmenu = Action {
    Ok(views.html.mainmenu("aaaa"))
  }
  def test = Action {request =>
    //userService.insert(User("aaa", "bbb"))
    val res  = userService.findUser("aaa","bbb")
    Ok(res.toString)
  }

}
