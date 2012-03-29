package redis.interchange.hrd

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */

@RunWith(classOf[JUnitRunner])
class ParsingSuite extends FunSuite {

  test("example from demonstration works fine") {
    val parser = new HrdParser

    val result = parser.parseAll(parser.dump, """
    "key"       : "value"
    "hash"      : {"hashKey" : 1, "anotherKey" : "value"}
    "set"       : #{1 2 3}
    "sortedSet" : #{ (1 "firstEntry") (0.1 "anotherEntry") }
    "list"      : [1 "hello" "world"]
    "binary"    : |aGVsbG8=|
    """)

    println(result);

    result match {
      case parser.Success(_, _) =>
      case parser.Error(msg, _) => fail(msg)
    }
  }

}
