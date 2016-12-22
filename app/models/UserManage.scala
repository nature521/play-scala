package models
import java.util.Date
import javax.inject.Inject
import anorm.SqlParser
import anorm

import play.api.db.DBApi
case class UserManage(Id:Option[Long],
                       UserName: String,
                       Password: String,
                       ConfigPost: String,
                       ConfigPort: String,
                       ConfigUserName: String,
                       ConfigPassword: String,
                       IsAdmini: Boolean,
                       Remark: String)
@javax.inject.Singleton
class UserManage @Inject()(dbapi:DBApi, companyService: CompanyService){
  private val db = dbapi.database("default")
  val simple = {

  }

}
// -- Parsers
/**
 * Parse a Computer from a ResultSet
 */


