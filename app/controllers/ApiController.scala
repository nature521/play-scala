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
    val userCount : Long = userService.findUser(userName, password)
    val userId : Long = userService.finIdByUserName(userName)
    if(userCount > 0){
      Ok("Welcome~").withSession(
        request.session +  ("UserName" -> userName) + ("UserId" -> userId.toString)
      )
    }else{
      Status(500)
    }
  }

  def mainmenu = Action {
    Ok(views.html.mainmenu("aaaa"))
  }

  def addUser = Action { request =>
    var userName: String = request.body.asFormUrlEncoded.get("userName")(0)
    var password: String = request.body.asFormUrlEncoded.get("password")(0)
    var isadmini= request.body.asFormUrlEncoded.get("isAdmini")(0)
    if(userService.findUserName(userName).toString().toInt > 0){
      Status(900)
    }
    if(isadmini.equals("true")){
      userService.insert(User(userName, password,true))
    }else{
      userService.insert(User(userName, password,false))
    }
    Ok("insertOk")
  }

  def addConfig(userId : Session) = Action { request =>
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

  def getAllUserAndConfigs() =  Action {implicit request =>
    val userAndCongigs = configService.getAll()
    val userAndCongigsList : List[(Config, User)]= userAndCongigs.toList
    //val configList:List[(Config)] = userAndCongigs.toList
    Ok(views.html.usermanageview.render(userAndCongigsList))
  }

  def getConfigsByUser(userId : Long) = Action { implicit request =>
    val configsList : List[(Config)] = configService.getConfigsByUser(userId).toList
    Ok(views.html.configmanage.render(configsList))
  }


  def test = Action {request =>
    //userService.insert(User("aaa", "bbb"))
    val res  = userService.findUserName("aaa")
    Ok(res.toString)
  }

}
