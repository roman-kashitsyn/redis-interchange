package redis.interchange.hrd

import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */

case class TimeSpan (seconds: Long) {
  def days    = TimeUnit.DAYS.convert(seconds, TimeUnit.SECONDS)
  def hours   = TimeUnit.HOURS.convert(seconds, TimeUnit.SECONDS)
  def minutes = TimeUnit.MINUTES.convert(seconds, TimeUnit.SECONDS)
}

object TimeSpan {
  val TimeSpanRegex =
    """^(\d{1,10}d)?(\d{1,10}h)?(\d{1,10}m)?(\d{1,10}s)?$""".r

  def apply(value: String) = {
    def skipLetter(interval: String) = interval.substring(0, interval.length - 1)
    def toNumber(interval: String) = if (interval != null) skipLetter(interval).toLong else 0L

    value.trim match {
      case TimeSpanRegex(days, hours, minutes, seconds) => {
        var secondsTotal = 0L
        secondsTotal += TimeUnit.SECONDS.convert(toNumber(days), TimeUnit.DAYS)
        secondsTotal += TimeUnit.SECONDS.convert(toNumber(hours), TimeUnit.HOURS)
        secondsTotal += TimeUnit.SECONDS.convert(toNumber(minutes), TimeUnit.MINUTES)
        secondsTotal += toNumber(seconds)
        new TimeSpan(secondsTotal)
      }
      case _ => throw new RuntimeException("Invalid time span string: " + value)
    }
  }
}
