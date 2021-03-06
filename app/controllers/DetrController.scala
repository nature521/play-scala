package controllers

import java.io._
import java.nio.file.attribute.PosixFilePermission._
import java.nio.file.attribute.PosixFilePermissions
import java.nio.file.{Files, Path}
import java.util
import javax.inject._

import akka.stream.IOResult
import akka.stream.scaladsl._
import akka.util.ByteString
import com.kunpeng.detr.ExcelDealer
import com.kunpeng.detr.entity.DetrResultJV
import com.kunpeng.detr.entity.DetrResultJV
import com.kunpeng.detr.model.ByteAndDetr
import models.{Config, ConfigService, DetrResult, DetrResultService}

import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.MessagesApi
import play.api.libs.streams._
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._
import play.core.parsers.Multipart.FileInfo

import scala.collection.JavaConversions
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import play.api.Logger

case class FormData(name: String)

/**
 * This controller handles a file upload.
 */
@Singleton
class DetrController @Inject()(detrService : DetrResultService)(configService : ConfigService) (implicit val messagesApi: MessagesApi) extends Controller {
val logger = Logger(this.getClass())
  //private val logger = org.slf4j.LoggerFactory.getLogger(this.getClass)

  val form = Form(
    mapping(
      "name" -> text
    )(FormData.apply)(FormData.unapply)
  )

  /**
   * Renders a start page.
   */

  def uploadView = Action { implicit request =>
    val userId : Long = request.session.get("UserId").get.toLong
    val isAdmini : String = request.session.get("IsAdmin").get
    val configList : List[Config] = configService.list(userId)
    val configStrList : List[String] = getEtermCongfig(configList)
    if(isAdmini.equals("true")){
      Ok(views.html.detr.uploadView(configStrList))
    }else{
      Ok(views.html.detr.ordinaryUserUploadView(configStrList))
    }

  }

  def ordinaryUserUploadView = Action { implicit request =>
    val userId : Long = request.session.get("UserId").get.toLong
    val configList : List[Config] = configService.list(userId)
    val configStrList : List[String] = getEtermCongfig(configList)
    Ok(views.html.detr.ordinaryUserUploadView(configStrList))
  }

  def getEtermCongfig(configList:List[Config]) : List[String] = {
    val length = configList.length
    var configBuffer: ListBuffer[String] = new ListBuffer[String]
    for (i <- 0 to length - 1) {
      var configStr: String = configList(i).ConfigPost + ":" + configList(i).ConfigPort + ":" + configList(i).ConfigName + ":" + configList(i).ConfigPassword
      configBuffer.append(configStr)
    }
    configBuffer.toList
  }
  type FilePartHandler[A] = FileInfo => Accumulator[ByteString, FilePart[A]]

  /**
   * Uses a custom FilePartHandler to return a type of "File" rather than
   * using Play's TemporaryFile class.  Deletion must happen explicitly on
   * completion, rather than TemporaryFile (which uses finalization to
   * delete temporary files).
   *
   * @return
   */
  private def handleFilePartAsFile: FilePartHandler[File] = {
    case FileInfo(partName, filename, contentType) =>
      val attr = PosixFilePermissions.asFileAttribute(util.EnumSet.of(OWNER_READ, OWNER_WRITE))
      val path: Path = Files.createTempFile("multipartBody", "tempFile", attr)
      val file = path.toFile
      val fileSink: Sink[ByteString, Future[IOResult]] = FileIO.toFile(file)
      val accumulator: Accumulator[ByteString, IOResult] = Accumulator(fileSink)
      accumulator.map {
        case IOResult(count, status) =>
          //logger.info(s"count = $count, status = $status")
          FilePart(partName, filename, contentType, file)
      }(play.api.libs.concurrent.Execution.defaultContext)
  }

  /**
   * A generic operation on the temporary file that deletes the temp file after completion.
   */
  private def operateOnTempFile(file: File) = {
    val size = Files.size(file.toPath)
    //logger.info(s"size = ${size}")
    Files.deleteIfExists(file.toPath)
    size
  }

  /**
   * Uploads a multipart file as a POST request.
   *
   * @return
   */
  def upload = Action(parse.multipartFormData) {implicit request =>

    var config : String = request.body.asFormUrlEncoded.get("config").get(0)
    request.body.file("excel").map { excel =>
    import java.io.File
      val filename = excel.filename
      val contentType = excel.contentType
      //val file = new File(s"/tmp/excel/$filename")
      val userId = request.session.get("UserId").get
      val file = new File(filename)
      excel.ref.moveTo(file, replace = true)
      println("file upload ok")
      logger.info("file upload ok:" + filename)
      // excel deal logical
      val byteAndDetrList : ByteAndDetr =  ExcelDealer.ExcelDeal(new FileInputStream(file),config)
      val byteArray  = byteAndDetrList.getBytes
      val detrResultList : List[DetrResultJV] = JavaConversions.asScalaBuffer(byteAndDetrList.getDetrResultList).toList
      insertDetrResult(detrResultList)
      Ok(byteArray).as("application/vnd.ms-excel").withHeaders(("Content-disposition", "attachment; filename=export.xls"))
    }.getOrElse {
      println("file upload error")
      Redirect(routes.DetrController.uploadView()).flashing(
        "error" -> "Missing file")
    }
  }



  private def insertDetrResult(detrResultList : List[DetrResultJV]){
    val length : Int = detrResultList.size
    if(length > 0){
      for(i <- 0 to length - 1){
        detrService.insert(DetrResult(detrResultList(i).ticketNum, detrResultList(i).ticketStatus,
          detrResultList(i).airCompany, detrResultList(i).flightNum, detrResultList(i).cabin,
          detrResultList(i).departureDate, detrResultList(i).ticketPrice,
          detrResultList(i).airRange, detrResultList(i).keyCount , detrResultList(i).useStatus,
          detrResultList(i).airCode, detrResultList(i).departureDate2, detrResultList(i).createTime,
          detrResultList(i).remark))
      }
    }

  }


}

