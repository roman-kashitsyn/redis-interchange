package redis.interchange.hrd

/**
 * @author Roman Kashitsyn
 */

object TestData {

  val DemoExample = """
    -- Comment line
    "key"       : "value"
    "hash"      : {"hashKey" : 1, "anotherKey" : "value"}
    "set"       : #{1 2 3}
    "sortedSet" : #{ (1 "firstEntry") (0.1 "anotherEntry") }
    "list"      : [1 "hello" "world"]
    "binary"    : |aGVsbG8=|
    """

}
