package models
import java.util.Date
import javax.inject.Inject
import anorm.SqlParser
import anorm._

import play.api.db.DBApi
case class UserManage(UserName: String,
                       Password: String)
@javax.inject.Singleton
class UserManageService @Inject()(dbapi:DBApi) {

  private val db = dbapi.database("default")


  def insert(userManage: UserManage) = {
    db.withConnection { implicit connection =>
      SQL(
        """
          insert into usermanage values (
            {username}, {password}
          )
        """
      ).on(
        'username -> userManage.UserName,
        'password -> userManage.Password
      ).executeUpdate()
    }
  }

}
// -- Parsers
/**
 * Parse a Computer from a ResultSet
 */


