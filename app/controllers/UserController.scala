package controllers

import javax.inject._

import models.{UserService, User}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UserController @Inject() (userService: UserService)extends Controller {

  def listUser() =  Action { implicit request =>
    Ok(views.html.user.list.render(userService.list))
  }

  def addUser() = Action { request =>
    val userName: String = request.body.asFormUrlEncoded.get("username").head
    val password: String = request.body.asFormUrlEncoded.get("password").head
    val isAdmin = request.body.asFormUrlEncoded.get("isAdmini").head
    if(userService.findUserName(userName).toString.toInt > 0){
      Status(500)
    }
    userService.insert(User(None, userName, password, isAdmin.equals("true")))
    Ok("insertOk")
  }

  def delUser(userId: Long) = Action { implicit request =>
    userService.delete(userId)
    Ok("ok")
  }
}
