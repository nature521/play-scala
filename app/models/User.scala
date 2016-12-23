package models
import java.util.Date
import javax.inject.Inject
import anorm.SqlParser
import anorm._
import scala._
import anorm.SqlParser._
import play.api.db.DBApi
case class User(UserName: String,
                       Password: String)


@javax.inject.Singleton
class UserService @Inject()(dbapi:DBApi) {

  private val db = dbapi.database("default")

  def insert(user: User) = {
    db.withConnection { implicit connection =>
      SQL(
        """
          insert into User values (
            {username}, {password}
          )
        """
      ).on(
        'username -> user.UserName,
        'password -> user.Password
      ).executeUpdate()
    }
  }
  //val Int = SQL("SELECT * FROM test").as((int("id") <~ str("val")).single)
  def  findUser(userName: String, password: String) = {
    var count : Long = 0

    db.withConnection{ implicit connection =>
       count = SQL("select count(*) from UserManage" +
        " where UserName = {UserName} " +
        "and Password = {Password}").on(
        'UserName -> userName,
        'Password -> password
      ).as(scalar[Long].single)
    }
    count
  }
}
// -- Parsers
/**
 * Parse a Computer from a ResultSet
 */


