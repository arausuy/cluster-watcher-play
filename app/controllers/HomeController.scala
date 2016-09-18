package controllers

import javax.inject._

import play.api.Logger
import play.api.libs.json.{JsSuccess, Json}
import play.api.libs.ws.{WSAuthScheme, WSClient, WSResponse}
import play.api.mvc._
import utils.{ClusterHealth, ClusterSize, PodServiceEntry}
import utils.ClusterHealth._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class HomeController @Inject()(ws: WSClient) extends Controller {


  def index = Action.async {
    //    Ok("Will put a README here")
    //
    //    clusterHealth(List("localhost"), "9002").map { res =>
    //      Ok(views.html.main("http-gateway", res, ClusterSize(res)))
    //    }

    Future {
      Ok("Will put a README here")
    }
  }


  def serviceHealth(serviceName: String, port: String, namespace: String = "default") = Action.async {
    val status = for {
      endPoints: Seq[String] <- getEndpoints(serviceName, namespace)
      health: Seq[ClusterEntry] <- clusterHealth(endPoints, port)
    } yield (health)

    status.map(res => Ok(views.html.main(serviceName, res, ClusterSize(res)))
    )
  }

  def getEndpoints(serviceName: String, namespace: String): Future[Seq[String]] = {
    val request = ws.url(s"https://kubernetes.default/api/v1/namespaces/$namespace/endpoints/$serviceName")
      .withAuth("development", "development", WSAuthScheme.BASIC)
      .get().recover { case _ => List.empty[String] }

    request map {
      case res: WSResponse => PodServiceEntry(Json.parse(res.body))
      case y: List[String] => {
        Logger.error("Unable to connect to kubernetes")
        y
      }
    }
  }

  def clusterHealth(podIps: Seq[String], port: String): Future[Seq[ClusterEntry]] = {
    Future.sequence(podIps.map { ip => getMemberDetails(ip, port) })
  }

  def getMemberDetails(ip: String, port: String): Future[ClusterEntry] =
    ws.url(s"http://$ip:$port/clusterhealth").get().map { res =>
      Json.fromJson[ClusterHealth](Json.parse(res.body))
    }.map {
      case JsSuccess(succ, _) => (ip, Right(Some(succ)))
      case _ => (ip, Right(None))
    }.recover {
      case e: Exception => {
        Logger.error("Timeout: Unable to connect to pod")
        Logger.error(e.getStackTraceString)
        (ip, Left("Timeout: Unable to connect to pod"))
      }
    }


}
