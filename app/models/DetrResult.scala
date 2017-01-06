package models
import java.util.Date
import javax.inject.Inject
import anorm.SqlParser
import anorm._
import scala._
import anorm.SqlParser._
import play.api.db.DBApi

case class DetrResult(TicketNum: Long,
                      TicketStatus : String, AirCompany : String,
                      FlightNum : String, Cabin :String,
                      DepartureDate : String, TicketPrice : Int,
                      AirRange : String, KeyAccount : String,
                      UseStatus : Int, AirCode : String,
                      DepartureDate2 : Date, CreateTime : Date,
                      Remark : String)

@javax.inject.Singleton
class DetrResultService @Inject()(dbapi: DBApi) {

  private val db = dbapi.database("default")

  def insert(detrResult: DetrResult) = {
    db.withConnection{ implicit  connection =>
      SQL(
        """
          insert into DetrResult (TicketNum, TicketStatus, AirCompany, FlightNum,
          Cabin, DepartureDate, TicketPrice, AirRange, KeyAccount, UseStatus, AirCode,
          DepartureDate2, CreateTime, Remark)
           values (
            {TicketNum},{TicketStatus},
            {AirCompany},{FlightNum},{Cabin},{DepartureDate},
            {TicketPrice},{AirRange},{KeyAccount},{UseStatus},
            {AirCode},{DepartureDate2},{CreateTime},{Remark}
          )
        """
      ).on(
        'TicketNum -> detrResult.TicketNum,
        'TicketStatus -> detrResult.TicketStatus,
        'AirCompany -> detrResult.AirCompany,
        'FlightNum -> detrResult.FlightNum,
        'Cabin -> detrResult.Cabin,
        'DepartureDate -> detrResult.DepartureDate,
        'TicketPrice -> detrResult.TicketPrice,
        'AirRange -> detrResult.AirRange,
        'KeyAccount -> detrResult.KeyAccount,
        'UseStatus -> detrResult.UseStatus,
        'AirCode -> detrResult.AirCode,
        'DepartureDate2 -> detrResult.DepartureDate2,
        'CreateTime -> detrResult.CreateTime,
        'Remark -> detrResult.Remark
      ).executeUpdate()
    }
  }
}
