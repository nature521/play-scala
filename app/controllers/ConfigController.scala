package controllers

import javax.inject._

import models.{Config, ConfigService, UserService, User}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ConfigController @Inject() (configService: ConfigService)extends Controller {

  def addConfig() = Action { request =>
    var configPost: String = request.body.asFormUrlEncoded.get("configPost")(0)
    var configPort: Int = request.body.asFormUrlEncoded.get("configPort")(0).toInt
    var configName: String = request.body.asFormUrlEncoded.get("configName")(0)
    var configPassword: String = request.body.asFormUrlEncoded.get("configPassword")(0)
    var userId : Long = request.session.get("UserId").get.toLong
    println(userId)
    configService.insert(Config(None,userId, configPost, configPort, configName, configPassword))
    Ok("insertOk")
  }

  def addConfigByUId(userId : Long) = Action {  implicit request =>
      var configPost: String = request.body.asFormUrlEncoded.get("configPost")(0)
      var configPort: Int = request.body.asFormUrlEncoded.get("configPort")(0).toInt
      var configName: String = request.body.asFormUrlEncoded.get("configName")(0)
      var configPassword: String = request.body.asFormUrlEncoded.get("configPassword")(0)
      configService.insert(Config(None,userId, configPost, configPort, configName, configPassword))
      Ok("insertOk")
  }
  def listConfig(userId : Long) = Action { implicit request =>
    val configsList : List[(Config)] = configService.list(userId).toList
    println(userId)
    Ok(views.html.config.listbyuser.render(configsList, userId))
  }

  def listConfigSelf() = Action { implicit request =>
    val userId : Long = request.session.get("UserId").get.toLong
    println(userId)
    val configsList : List[(Config)] = configService.list(userId).toList
    val isAdmini : String = request.session.get("IsAdmin").get
    if(isAdmini.equals("true")){
      Ok(views.html.config.list.render(configsList))
    }else{
      Ok(views.html.config.ordinaryUserList.render(configsList))
    }

  }


  def delConfig(id : Long) = Action { implicit request =>
    configService.del(id)
    Ok("Ok~")
  }
}
