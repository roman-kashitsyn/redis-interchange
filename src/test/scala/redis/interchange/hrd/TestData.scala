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

    -- Keys with expiration
    "exp1" [1341922275] : "value" -- Expires at 2012-10-07T07:11:15
    "exp2" [1d5h]       : "value" -- Expires after 1 day and 5 hours
    """

}
