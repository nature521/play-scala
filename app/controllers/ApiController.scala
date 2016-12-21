package controllers

import javax.inject.{Singleton, Inject}

import play.api.mvc.{Action, Controller}

/**
 * Created by Administrator on 2016/12/21.
 */
@Singleton
class ApiController  @Inject() extends Controller{
  def login = Action {request =>
    Ok("sucesss")
  }
  def mainmenu = Action {
    Ok(views.html.mainmenu("this is the message"))
  }

}
