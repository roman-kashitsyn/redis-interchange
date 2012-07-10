package redis.interchange.hrd

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FunSuite

/**
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
@RunWith(classOf[JUnitRunner])
class TimeSpanSuite extends FunSuite {

  test("TimeSpan should work properly with single intervals") {
    val values = Map(
      "5d" -> 5 * 24 * 60 * 60L,
      "5h" -> 5 * 60 * 60L,
      "5m" -> 5 * 60L,
      "5s" -> 5L
    )
    for { (text, seconds) <- values } {
      assert(TimeSpan(text) == TimeSpan(seconds),
        "Timespan %s is not present as %d seconds".format(text, seconds))
    }
  }

  test("TimeSpan should return right interval values") {
    val span = TimeSpan("5d6h7m8s")
    assert(span.days == 5)
    assert(span.hours == span.days * 24 + 6)
    assert(span.minutes == span.hours * 60 + 7)
    assert(span.seconds == span.minutes * 60 + 8)
  }
}
