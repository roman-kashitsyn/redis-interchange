package redis.interchange.hrd

import scala.util.parsing.combinator.JavaTokenParsers

/**
 * HRD Parser definition.
 * @author Roman Kashitsyn
 */

class HrdParser extends JavaTokenParsers {
  def dump         = rep(entry)
  def entry        = atom ~ space ~':' ~ space ~ ( atom | hash | list | set | sortedSet )
  def atom         = floatingPointNumber | stringLiteral | bytes
  def bytes        = '|' ~> base64 <~ '|'
  def base64       = """[a-zA-Z0-9+/]+(=){0,2}""".r
  def list         = '[' ~> space ~> rep(atom) <~ space <~ ']'
  def hash         = '{' ~> space ~> repsep(keyValuePair, ',') <~ space <~ '}'
  def keyValuePair = atom ~ space ~ ':' ~ space ~ atom
  def set          = "#{" ~> space ~> rep(atom) <~ space <~ '}'
  def sortedSet    = "#{" ~> space ~> repsep(scoredPair, space) <~ space <~ '}'
  def scoredPair   = '(' <~ space ~> floatingPointNumber <~ space ~> atom <~ space ~> ')'

  def space: Parser[Any]        = """\s*""".r
}
