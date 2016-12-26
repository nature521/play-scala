package controllers

import javax.inject._

import anorm.SqlQueryResult
import models._
import play.api._
import play.api.libs.json
import play.api.mvc._
import play.api.libs.functional.syntax._
import views.html.usermanageview

/**
 * Created by Administrator on 2016/12/21.
 */
@Singleton
class ApiController  @Inject() (userService: UserService, configService: ConfigService,
                                userWithConfigService: UserWithConfigService
                                 )extends Controller{
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
  var userName: String = request.body.asFormUrlEncoded.get("userName")(0)
  var password: String = request.body.asFormUrlEncoded.get("password")(0)
  var configPost: String = request.body.asFormUrlEncoded.get("configPost")(0)
  var configPort: Int = request.body.asFormUrlEncoded.get("configPort")(0).toInt
  var configName: String = request.body.asFormUrlEncoded.get("configName")(0)
  var configPassword: String = request.body.asFormUrlEncoded.get("configPassword")(0)
  var isadmini= request.body.asFormUrlEncoded.get("isAdmini")(0)
  println(isadmini)
  if(isadmini.equals("true")){
    userService.insert(User(userName, password,true))
  }else{
    userService.insert(User(userName, password,false))
  }
  configService.insert(Config(userName, configPost, configPort, configName, configPassword))
  Ok("insertOk")
}
  def getAllUserAndConfigs() =  Action {implicit request =>
    val userAndCongigs = configService.getAll()
    var userAndCongigsList : List[(Config, User)]= userAndCongigs.toList
    return Ok(views.html.usermanage.render(userAndCongigsList))
  }

  def test = Action {request =>
    //userService.insert(User("aaa", "bbb"))
    val res  = userService.findUser("aaa","bbb")
    Ok(res.toString)
  }



}
