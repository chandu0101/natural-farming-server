package com.chandu0101.nfarming.json

import com.chandu0101.nfarming.error.Error
import com.chandu0101.nfarming.model.Seed
import spray.json.DefaultJsonProtocol

/**
 * Created by chandrasekharkode on 11/11/14.
 */
object NFarmingJsonProtocol extends DefaultJsonProtocol{

  implicit  val seedJson = jsonFormat5(Seed.apply)
  implicit  val errorJson = jsonFormat3(Error)
}
