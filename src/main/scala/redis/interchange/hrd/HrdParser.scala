package redis.interchange.hrd

import scala.util.parsing.combinator.JavaTokenParsers

/**
 * HRD Parser definition.
 * @author Roman Kashitsyn
 */

class HrdParser extends JavaTokenParsers {
  def dump         = rep(entry)
  def entry        = atom ~ ":" ~ ( atom | hash | list | sortedSet | set )
  def atom         = floatingPointNumber | stringLiteral | bytes
  def bytes        = "|" ~> base64 <~ "|"
  def base64       = """[a-zA-Z0-9+/]+(=){0,2}""".r
  def list         = "[" ~> rep(atom) <~ "]"
  def hash         = "{" ~> repsep(keyValuePair, ",") <~ "}"
  def keyValuePair = atom <~ ":" ~ atom
  def set          = "#{" ~> rep(atom) <~ "}"
  def sortedSet    = "#{" ~> rep(scoredPair) <~ "}"
  def scoredPair   = "(" ~> floatingPointNumber ~ atom <~ ")"

}
