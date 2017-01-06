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
      Ok(users.head.IsAdmini.toString).withSession(
        request.session +  ("Username" -> users.head.UserName) + ("UserId" -> users.head.Id.get.toString)
      )
    }else{
      Status(500)
    }
  }


  def test = Action {request =>
    //userService.insert(User("aaa", "bbb"))
    val res  = userService.findUserName("aaa")
    Ok(res.toString)
  }


}
