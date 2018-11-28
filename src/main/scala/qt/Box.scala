
package qt

case class Box(center:Point, half_dim:Point) {

  def contains_point(point:Point) : Boolean = {
    contains_point(point.x, point.y)
  }

  private def contains_point(x: Double, y:Double) : Boolean = {
    x >= left && x <= right &&
    y >= bottom && y <= top
  }

  def intersects(other:Box) : Boolean = {
    lazy val down_right = contains_point(other.bottom, other.right)
    lazy val down_left = contains_point(other.bottom, other.left)
    lazy val up_right = contains_point(other.top, other.right)
    lazy val up_left = contains_point(other.top, other.left)
    
    up_left || up_right || down_left || down_right
  }

  lazy val quarter_dim = half_dim.half()

  def up_left_quadrant()
    = Box(Point(center.x - quarter_dim.x, center.y + quarter_dim.y), quarter_dim)

  def up_right_quadrant()
    = Box(Point(center.x + quarter_dim.x, center.y + quarter_dim.y), quarter_dim)

  def down_left_quadrant()
    = Box(Point(center.x - quarter_dim.x, center.y - quarter_dim.y), quarter_dim)

  def down_right_quadrant()
    = Box(Point(center.x + quarter_dim.x, center.y - quarter_dim.y), quarter_dim)

  private def top: Double = {
    center.y + half_dim.y
  }

  private def bottom: Double = {
    center.y - half_dim.y
  }

  private def right: Double = {
    center.x + half_dim.x
  }

  private def left: Double = {
    center.x - half_dim.x
  }

}