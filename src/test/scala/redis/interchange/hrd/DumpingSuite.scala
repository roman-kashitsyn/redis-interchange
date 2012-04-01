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

  test("dump of demo example works") {
    val config = RedisConfig()
    new Dumper().importAll(config, TestData.DemoExample)


    val jedis: Jedis = new Jedis(config.host, config.port)
    assert("value" == jedis.get("key"))
    assert(jedis.smembers("set").size() == 3)
    assert(jedis.zcount("sortedSet", 0, 1) == 2L)
    assert(jedis.llen("list") == 3L)
  }

}