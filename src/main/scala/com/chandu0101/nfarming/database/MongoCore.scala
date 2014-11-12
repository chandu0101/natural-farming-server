package com.chandu0101.nfarming.database

import reactivemongo.api.{DefaultDB, MongoDriver}
import reactivemongo.core.nodeset.Authenticate

trait MongoCore {
  implicit  val context = scala.concurrent.ExecutionContext.Implicits.global
  val driver = new MongoDriver()
  val dbName = "nfarming"
  val username = "chandu0101"
  val password = "dbpass"
  val credentials = List(Authenticate(dbName, username, password))
  val servers = List("ds051740.mongolab.com:51740")
  val connection = driver.connection(servers, nbChannelsPerNode = 5, authentications = credentials)
//  val connection = driver.connection(List("localhost"))

  val nfarming = connection.db("nfarming")
}