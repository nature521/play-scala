package models
import anorm._
import scala._
import anorm.SqlParser._
import javax.inject.Inject
import play.api.db.DBApi

/**
 * Created by Administrator on 2016/12/23.
 */
@javax.inject.Singleton
case class Config(UserName: String, ConfigPost: String, ConfigPort: Int, ConfigName: String, ConfigPassword: String)
case class  UserWithConfig(UserName: String, Password: String, IsAdmini: Boolean,
                           ConfigPost : String, configPort : Int,
                           ConfigName :String, configPassword :String)

case class Page[A](items:Seq[A], page:Int, offset: Long, total: Long){
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ =>(offset + items.size) < total)
}



@javax.inject.Singleton
class ConfigService @Inject()(dbapi: DBApi, userService: UserService) {
  private val db = dbapi.database("default")
  val simple = {
    get[String]("Config.UserName") ~
      get[String]("Config.ConfigPost") ~
      get[Int]("Config.ConfigPort") ~
      get[String]("Config.ConfigName") ~
      get[String]("Config.ConfigPassword")  map{
      case username~configPost~configPort~configName~configPassword =>
        Config(username, configPost, configPort, configName, configPassword)
    }
  }
  val userWithConfigSim = simple ~ (userService.simple ) map{
    case config~user => (config, user)
  }

  def insert(config : Config) = {
    db.withConnection { implicit connection =>
      SQL("insert into Config values ( {username},{configpost},{configport},{configname},{configpassword})"
      ).on(
        'username -> config.UserName,
        'configpost -> config.ConfigPost,
        'configport -> config.ConfigPort,
        'configname -> config.ConfigName,
        'configpassword -> config.ConfigPassword
      ).executeUpdate()
    }
  }
  def getAll()= {
      db.withConnection { implicit connection =>
        val result = SQL(
          """
             select * from Config Full join UserManage on config.UserName = UserManage.UserName
          """
        ).as(userWithConfigSim *)
        result
      }
  }
}
