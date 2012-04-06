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


  def dump: Parser[Map[Any, Any]]         = rep(entry) ^^ {Map() ++ _}
  def entry: Parser[(Any, Any)]           = atom ~ ":" ~ ( atom | hash | list | sortedSet | set ) ^^ {case k ~ ":" ~ v => (k ,v)}
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
    def compare(a: (Double, Any), b: (Double, Any)) = a._1 compare b._1
  }

  private def unquote(s: String) = s.substring(1, s.length - 1)

}
