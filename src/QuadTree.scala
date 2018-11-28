
import scalafx.scene.shape.{Line, Circle}
import scalafx.scene.paint.Color
import scala.io.Source

import scala.collection.mutable.MutableList
import QuadTree._

class QuadTree(boundary:Box) {

  private val points = MutableList[Point]()

  private var up_left = unused
  private var up_right = unused
  private var down_left = unused
  private var down_right = unused

  // insert 
  def insert(point:Point):Boolean = {
    if (!boundary.contains_point(point))
      return false

    if (points.size < capacity) {
      points += point
      return true
    }

    if (isLeaf)
      split()

    return (up_left.insert(point) || up_right.insert(point)
        || down_left.insert(point) || down_right.insert(point))
  }

  // query
  def range_query(range:Box) : MutableList[Circle] = {
    val pmatches = MutableList[Point]()
    val cmatches = MutableList[Circle]()
    
    range_query(range, pmatches)
    
    pmatches.foreach(
          knn => cmatches += new Circle{
            centerX = knn.x
            centerY = knn.y
            radius = 1
            stroke = Color.BLUE            
          }                    
      )      
    cmatches
  }

  private def range_query(range:Box, matches:MutableList[Point]) : Unit = {
    /*if (!boundary.intersects(range))
      return*/
    
    points.filter(range.contains_point(_))
          .foreach(matches += _)
    
    if (!isLeaf) {
      up_left.range_query(range, matches)
      up_right.range_query(range, matches)
      down_left.range_query(range, matches)
      down_right.range_query(range, matches)
    }
  }
  
  // lines
  def get_lines(lines: MutableList[Line]): Unit = {
    
    if(isLeaf)
      return    
    
    var line1 = Line(
        boundary.center.x - boundary.half_dim.x, boundary.center.y, 
        boundary.center.x + boundary.half_dim.x, boundary.center.y
    )
    
    var line2 = Line(
        boundary.center.x, boundary.center.y - boundary.half_dim.y, 
        boundary.center.x, boundary.center.y + boundary.half_dim.y
    )
    
    line1.setStroke(Color.GREEN)
    line2.setStroke(Color.GREEN)
    
    lines += line1
    lines += line2
        
    up_left.get_lines(lines)
    up_right.get_lines(lines)
    down_left.get_lines(lines)
    down_right.get_lines(lines)
  }
  
  def get_lines(): MutableList[Line] = {
    var lines = MutableList[Line]()    
    get_lines(lines)
    lines
  }

  // leaf
  def isLeaf: Boolean = {
    up_left == unused
  }

  // split
  def split() {
    up_left = new QuadTree(boundary.up_left_quadrant())
    up_right = new QuadTree(boundary.up_right_quadrant())
    down_left = new QuadTree(boundary.down_left_quadrant())
    down_right = new QuadTree(boundary.down_right_quadrant())
  }  

  // load csv
  def load_csv(circles: MutableList[Circle], filename: String): Unit = {
    val file = Source.fromFile(filename)
      for (tline <- file.getLines) {
          val cols = tline.split(",").map(_.trim)
          circles += new Circle{
            centerX = cols(0).toInt
            centerY = cols(1).toInt
            radius = 1
            stroke = Color.RED
          }
          insert(Point(cols(0).toInt,cols(1).toInt))
      }
      
      file.close
  }
}

object QuadTree {
  private val capacity = 4
  private val unused:QuadTree = null
}