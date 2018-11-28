
case class Point(x:Double, y:Double) {

  def half() = Point(x / 2, y / 2)
  
  override def toString(): String = {
    s"(${x},${y}) "
  }

}