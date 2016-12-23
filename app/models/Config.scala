package models

import javax.inject.Inject

import play.api.db.DBApi

/**
 * Created by Administrator on 2016/12/23.
 */
case class Config(ConfigPost: String, ConfigPort: String, ConfigName: String, ConfigPassword: String)

@javax.inject.Singleton
class ConfigService @Inject()(dbapi: DBApi) {
  private val db = dbapi.database("default")

}
