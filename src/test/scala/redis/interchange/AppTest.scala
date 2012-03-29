package redis.interchange;

import junit.framework._;
import Assert._;

object AppTest {
    def suite: Test = {
        val suite = new TestSuite(classOf[AppTest]);
        suite
    }

    def main(args : Array[String]) {
        junit.textui.TestRunner.run(suite);
    }
}

/**
 * Unit test for simple App.
 */
class AppTest extends TestCase("app") {

  def testParse() {
    import hrd.HrdParser

    val parser = new HrdParser

    println( parser.parseAll(parser.dump, """
    "key"       : "value"
    "hash"      : {"hashKey" : 1, "anotherKey" : "value"}
    "set"       : #{1 2 3}
    "sortedSet" : #{ (1 "firstEntry") (0.1 "anotherEntry")}
    "list"      : [1 "hello" "world"]
    "binary"    : |base64|
    """) );
  }
    

}
