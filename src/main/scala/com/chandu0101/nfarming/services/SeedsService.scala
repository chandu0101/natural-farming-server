package com.chandu0101.nfarming.services

import com.chandu0101.nfarming.database.MongoCore
import com.chandu0101.nfarming.error.Error
import com.chandu0101.nfarming.model.Seed
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.BSONDocument
import reactivemongo.core.commands.LastError
import spray.caching.{Cache, LruCache}

import scala.concurrent.Future

/**
 * Created by chandrasekharkode on 11/11/14.
 */
object SeedsService extends MongoCore {

  lazy val seedsCollection = initCollection

  val seedCache: Cache[List[Seed]] = LruCache()

  val SEEDS = "seeds"

  def initCollection = {
    val collection = nfarming[BSONCollection](SEEDS)
    collection.indexesManager.create(Index(List(("name" -> IndexType.Ascending)), unique = true))
    collection
  }

  def findAll(): Future[List[Seed]] = seedCache(SEEDS) {
    seedsCollection.find(BSONDocument.empty).cursor[Seed].collect[List]()
  }

  def insert(seed: Seed): Future[Seed] = {
    seedsCollection.insert[Seed](seed).map(lastError => mapLastError(lastError, seed))
  }

  def mapLastError[T](lastError: LastError, to: T) = lastError.inError match {
    case false => to
    case true => throw new Exception(lastError.message)
  }

  def update(seed: Seed): Future[Seed] = {
    seedsCollection.update(BSONDocument(Seed.NAME -> seed.name), seed).map(lastError => mapLastError(lastError, seed))
  }

  def delete(seed: Seed): Future[String] = {
    seedsCollection.remove(seed).map(lastError => mapLastError(lastError, "deleted"))
  }

  def clearChache() =  {
    seedCache.clear()
    "Success fully Cleared cache"
  }
}
