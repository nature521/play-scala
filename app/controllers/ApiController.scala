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

 def usermanage = Action {
   Ok(views.html.usermanage("aaaa"))
 }

def addUser = Action { request =>
  var userName: String = request.body.asFormUrlEncoded.get("user_name")(0)
  var password: String = request.body.asFormUrlEncoded.get("password")(0)
  var configPost: String = request.body.asFormUrlEncoded.get("config_post")(0)
  var configPort: String = request.body.asFormUrlEncoded.get("config_port")(0)
  var configName: String = request.body.asFormUrlEncoded.get("config_name")(0)
  var configPassword: String = request.body.asFormUrlEncoded.get("config_password")(0)
  userService.insert(User(userName, password))
  Ok("insertOk")
}





  def test = Action {request =>
    //userService.insert(User("aaa", "bbb"))
    val res  = userService.findUser("aaa","bbb")
    Ok(res.toString)
  }



}
