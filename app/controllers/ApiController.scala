package controllers

import javax.inject._

import anorm.SqlQueryResult
import models._
import play.api._
import play.api.libs.json
import play.api.mvc._
import play.api.libs.functional.syntax._


@Singleton
class ApiController  @Inject() (userService: UserService, configService: ConfigService,
                                userWithConfigService: UserWithConfigService
                                 )extends Controller{
  def login = Action {request =>
    var userName : String = request.body.asFormUrlEncoded.get("username").head
    var password : String = request.body.asFormUrlEncoded.get("password").head
    //var json = request.body.asJson
    //var userName = (json / "userName").get
    val users : List[User] = userService.findUser(userName, password)
    if(users.nonEmpty){
      Ok("Welcome~").withSession(
        request.session +  ("Username" -> users.head.UserName) + ("UserId" -> users.head.Id.get.toString)
      )
    }else{
      Status(500)
    }
  }


  def addConfig() = Action { request =>
    var password: String = request.body.asFormUrlEncoded.get("password")(0)
    var configPost: String = request.body.asFormUrlEncoded.get("configPost")(0)
    var configPort: Int = request.body.asFormUrlEncoded.get("configPort")(0).toInt
    var configName: String = request.body.asFormUrlEncoded.get("configName")(0)
    var configPassword: String = request.body.asFormUrlEncoded.get("configPassword")(0)
    var userId : Long = request.session.get("UserId").toString().toLong
    //var userId : Long = userService.finIdByUserName(userName)
    configService.insert(Config(userId, configPost, configPort, configName, configPassword))
    Ok("insertOk")
  }



  def getConfigsByUser(userId : Long) = Action { implicit request =>
    val configsList : List[(Config)] = configService.getConfigsByUser(userId).toList
    Ok(views.html.config.list.render(configsList))
  }


  def test = Action {request =>
    //userService.insert(User("aaa", "bbb"))
    val res  = userService.findUserName("aaa")
    Ok(res.toString)
  }

}
