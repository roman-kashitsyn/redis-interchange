package redis.interchange.hrd

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import collection.SortedSet

/**
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */

@RunWith(classOf[JUnitRunner])
class ParsingSuite extends FunSuite {

  test("example from demo works fine") {
    val parser = new HrdParser

    val result = parser.parseAll(parser.dump, TestData.DemoExample)

    println(result);

    result match {
      case parser.Success(_, _) => ()
      case parser.Error(msg, _) => fail(msg)
    }

    val dump = result.get

    assert(dump("list").isInstanceOf[List[Any]])
    assert(dump("set").isInstanceOf[Set[Any]])
    assert(dump("sortedSet").isInstanceOf[SortedSet[(Double, Any)]])
    assert(dump("hash").isInstanceOf[Map[Any, Any]])
    assert(dump("binary").isInstanceOf[Array[Byte]])
    assert(dump("key") == "value")
    assert(java.util.Arrays.equals(dump("binary").asInstanceOf[Array[Byte]], toAsciiBytes("hello")))
  }

  private def toAsciiBytes(str: String): Array[Byte] = str.map(_.toByte).toArray

}
