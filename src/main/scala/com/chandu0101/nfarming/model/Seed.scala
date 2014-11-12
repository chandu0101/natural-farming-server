package com.chandu0101.nfarming.model

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

/**
 * Created by chandrasekharkode on 11/10/14.
 */
case class Seed(name: String, category: String, duration: String, location: Position, info: String)

case class Position(long : Int, lat: Int)

object Position {

  val LONGITUDE = "lon"
  val LATITUDE = "lan"

  implicit object PositionReader extends BSONDocumentReader[Position] {
    override def read(bson: BSONDocument): Position = {
      val lon = bson.getAs[Int](LONGITUDE).get
      val lat = bson.getAs[Int](LATITUDE).get
      Position(lon, lat)
    }
  }

  implicit object PositionWriter extends BSONDocumentWriter[Position] {
    override def write(t: Position): BSONDocument = {
      BSONDocument(LONGITUDE -> t.long,
        LATITUDE -> t.lat)

    }
  }

}

object Seed {
  val ID = "_id"
  val NAME = "name"
  val CATEGORY = "category"
  val DURATION = "duration"
  val LOCATION = "location"
  val INFO = "info"

  // needed for reactive mongo
  implicit object SeedReader extends BSONDocumentReader[Seed] {
    def read(doc: BSONDocument): Seed = {
      val name = doc.getAs[String](NAME).get
      val category = doc.getAs[String](CATEGORY).get
      val duration = doc.getAs[String](DURATION).get
      val location = doc.getAs[Position](LOCATION).get
      val info = doc.getAs[String](INFO).get

      Seed(name, category, duration, location, info);
    }
  }

  implicit object SeedWriter extends BSONDocumentWriter[Seed] {
    def write(seed: Seed): BSONDocument = {
      BSONDocument(
        NAME -> seed.name,
        CATEGORY -> seed.category,
        DURATION -> seed.duration,
        LOCATION -> seed.location,
        INFO -> seed.info
      )
    }
  }


}