package redis.interchange.hrd

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import redis.clients.jedis.Jedis

/**
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */

@RunWith(classOf[JUnitRunner])
class DumpingSuite extends FunSuite {

  test("Dump of demo example exported successfully") {
    val dumper = new Dumper()
    val jedis: Jedis = new Jedis(dumper.host, dumper.port)
    jedis.flushAll()

    dumper.importAll(TestData.DemoExample)

    assert("value" == jedis.get("key"))
    assert(jedis.smembers("set").size() == 3)
    assert(jedis.zcount("sortedSet", 0, 1) == 2L)
    assert(jedis.llen("list") == 3L)
  }
}
