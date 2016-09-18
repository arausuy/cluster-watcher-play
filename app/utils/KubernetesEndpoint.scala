package utils

import play.api.libs.json.{JsValue}

object PodServiceEntry {
  def apply(jsVal: JsValue): Seq[String] = (jsVal \\ "ip").map(x => x.as[String])
}



