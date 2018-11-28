

class Utils {
  def random_between(start: Int, end: Int): Int = {
    val r = new scala.util.Random
    start + r.nextInt(( end - start) + 1)
  }
}