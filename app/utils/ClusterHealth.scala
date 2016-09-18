package utils

import play.api.libs.json.Json

case class Member(address: String, status: String)

case class ClusterHealth(members: List[Member], unreachable: List[Member], clustermembers: Int)

object Member {
  implicit val formats = Json.format[Member]
}

object ClusterHealth {
  type ClusterEntry = (String, Either[String, Option[ClusterHealth]])

  implicit val formats = Json.format[ClusterHealth]
}

object ClusterSize {
  def apply(serviceInstances: Seq[(String, Either[String, Option[ClusterHealth]])]) = {
    val clusterReturns = serviceInstances.map(x => x._2).flatMap(y => y.right.toOption).flatten

    clusterReturns.map(c => c.members.map(m => m.address)).distinct.size match {
      case 0 => "No pod reported back"
      case 1 => "All reporting pods have the same number of reachable members"
      case _ => "Pods have reported different cluster sizes : Possible in a split brain scenario"
    }
  }
}
