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
case class Config(Id: Option[Long] = None, UserId: Long, ConfigPost: String, ConfigPort: Int, ConfigName: String, ConfigPassword: String)
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
    get[Option[Long]]("Id") ~
    get[Long]("UserId") ~
      get[String]("ConfigPost") ~
      get[Int]("ConfigPort") ~
      get[String]("ConfigName") ~
      get[String]("ConfigPassword")  map{
      case id~userid~configPost~configPort~configName~configPassword =>
        Config(id,userid, configPost, configPort, configName, configPassword)
    }
  }


  val userWithConfigSim = simple ~ (userService.simple ) map{
    case config~user => (config, user)
  }

  def insert(config : Config) = {
    db.withConnection { implicit connection =>
      SQL("insert into Config values ( {userid},{configpost},{configport},{configname},{configpassword})"
      ).on(
        'userid -> config.UserId,
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
          select
          c.id [Config.Id], c.UserId [Config.UserId],
          c.ConfigPost [Config.ConfigPost],c.ConfigPort [Config.ConfigPort],
          c.ConfigName [Config.ConfigName],c.ConfigPassword [Config.ConfigPassword],
          u.UserName [User.UserName], u.Password [User.Password], u.IsAdmini [User.IsAdmini]
          from Config as c join UserManage as u on c.UserId = u.Id
      """
      ).as(userWithConfigSim *)
      result
    }
  }

  //根据用户查找配置，两种情况用，一是每个用户看自己的配置，另一个是管理员看用户的配置；
  def list(userId : Long)= {
    db.withConnection{ implicit connection =>
      val result = SQL(
      "select * from Config where UserId = {UserId}"
      ).on(
        'UserId -> userId
      ).as(simple *)
      result
    }
  }

  //删除配置，那只能根据每个配置的id删除；
  def del(id : Long) = {
    db.withConnection{ implicit connection =>
      SQL(
      "delete from Config where Id = {id}"
      ).on(
      'id -> id
      ).executeUpdate()
    }
  }

}
