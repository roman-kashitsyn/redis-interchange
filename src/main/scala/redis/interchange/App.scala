package redis.interchange;

/**
 * Hello world!
 *
 */
object App extends Application {
  import hrd.HrdParser

  val parser = new HrdParser

  println( parser.parseAll(parser.dump, """ "hello":1 """) );
}
