package redis.interchange.hrd

import scala.util.parsing.combinator.JavaTokenParsers
import collection.SortedSet

/**
 * HRD Parser definition.
 * @author Roman Kashitsyn
 */

class HrdParser extends JavaTokenParsers {

  // Use that for java-like comments
  // protected override val whiteSpace = """(\s|//.*|(?m)/\*(\*(?!/)|[^*])*\*/)+""".r

  protected override val whiteSpace = """(\s|--.*)+""".r
  protected val TimeSpanPattern = """(\d{1,10}[dhms])+""".r

  def dump: Parser[RedisDump]             = rep(entry) ^^ { new RedisDump(_) }
  def entry: Parser[(RedisKey, Any)]      = atom ~ opt(timeToLive) ~ ":" ~ ( atom | hash | list | sortedSet | set ) ^^ makeKeyValuePair
  def timeToLive: Parser[Expiration]      = "[" ~> (expiresAfter | unixTimestamp) <~ "]"
  def unixTimestamp: Parser[Expiration]   = """\d+""".r ^^ { s: String => UnixTimestamp(s.toLong) }
  def expiresAfter: Parser[Expiration]    = TimeSpanPattern ^^ { s: String => ExpirationTimeSpan(TimeSpan(s))}
  def atom: Parser[Any]                   = floatingPointNumber | stringLiteral ^^ unquote | bytes
  def bytes: Parser[Array[Byte]]          = "|" ~> base64 <~ "|" ^^ {new sun.misc.BASE64Decoder().decodeBuffer(_)}
  def base64: Parser[String]              = """[a-zA-Z0-9+/]+(=){0,2}""".r
  def list: Parser[List[Any]]             = "[" ~> rep(atom) <~ "]"
  def hash: Parser[Map[Any, Any]]         = "{" ~> repsep(keyValuePair, ",") <~ "}" ^^ { Map() ++ _ }
  def keyValuePair: Parser[(Any, Any)]    = atom ~ ":" ~ atom ^^ { case k ~ ":" ~ v => (k, v) }
  def set: Parser[Set[Any]]               = "#{" ~> rep(atom) <~ "}" ^^ {Set() ++ _}
  def sortedSet: Parser[SortedSet[(Double, Any)]] = "#{" ~> rep(scoredPair) <~ "}" ^^ {SortedSet()(ord=TupleOrdering) ++  _}
  def scoredPair: Parser[(Double, Any)]   = "(" ~> floatingPointNumber ~ atom <~ ")" ^^ {case f ~ a => (f.toDouble, a)}


  object TupleOrdering extends Ordering[(Double, Any)] {
    def compare(a: (Double, Any), b: (Double, Any)) = {
      val compareScores = a._1 compare b._1
      if (compareScores != 0) compareScores else compareValues((a._2, b._2))
    }

    private def compareValues(t: (Any, Any)): Int = {
      t match {
        case (x: Double, y: Double) => x compare y
        case (x: String, y: String) => x compare y
        case (xs: Array[Byte], ys: Array[Byte]) => compareArrays(xs, ys)
        case _ => 1
      }
    }

    private def compareArrays(xs: Array[Byte], ys: Array[Byte]): Int = {
      val xIt = xs.iterator
      val yIt = ys.iterator
      while (xIt.hasNext && yIt.hasNext) {
        val cmp = xIt.next() compare yIt.next()
        if (cmp != 0) return cmp
      }
      if (xIt.hasNext) 1 else if (yIt.hasNext) -1 else 0
    }
  }

  private def unquote(s: String) = s.substring(1, s.length - 1)

  private def makeKeyValuePair (value: Any) = value match {
    case k ~ ts ~ ":" ~ v => (RedisKey(k, ts.asInstanceOf[Option[Expiration]]), v)
  }
}
