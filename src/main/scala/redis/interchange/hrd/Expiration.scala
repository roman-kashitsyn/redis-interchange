package redis.interchange.hrd

/**
 * Redis key expiration time.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */

abstract sealed class Expiration

case class UnixTimestamp (timestamp: Long) extends Expiration {
  override def toString = timestamp.toString
}

case class ExpirationTimeSpan (timeSpan: TimeSpan) extends Expiration {
  override def toString = timeSpan.seconds.toString + "s"
}