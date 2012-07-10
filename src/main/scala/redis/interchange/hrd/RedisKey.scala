package redis.interchange.hrd

/**
 * Represent key in redis database.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
case class RedisKey (key: Any, expiration: Option[Expiration]) {
  override def toString = {
    if (expiration.isDefined)  (mkKey + " [" + expiration + "] ") else mkKey
  }

  private def mkKey = key match {
    case bytes: Array[Byte] => "|" + new sun.misc.BASE64Encoder().encode(bytes) + "|"
    case _ => key.toString
  }
}
