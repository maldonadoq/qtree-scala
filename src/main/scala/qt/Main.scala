package qt

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.shape.{Line, Rectangle, Circle}
import scalafx.scene.paint.Color
import scalafx.scene.input._

import scala.collection.mutable.MutableList

object Main extends JFXApp{
  val utils = new Utils()
  
  var circles = MutableList[Circle]()
  var matches = MutableList[Circle]()
  var lines = MutableList[Line]()
  
  var load: Boolean = true;
  var filename = "/home/maldonado/Qtree-Scala/src/main/scala/qt/Data.csv"

  var xsize:Int = 800
  var ysize:Int = 600
  
  var nc:Int = 100
  var sep: Int = 10
  var nar: Int = 5
  var rd: Int = 1 
  
  var ran: Int = 50
  
  val boundary = Box(Point(xsize/2,ysize/2), Point(xsize/2,ysize/2))  
  val qt = new QuadTree(boundary)
     
  stage = new PrimaryStage {
    title = "QuadTree"
    scene = new Scene (xsize,ysize){                  
      
      onKeyPressed = (ke: KeyEvent) => {
        if(ke.code == KeyCode.Space && load) {                            
          qt.load_csv(circles, filename)          
          content = circles
          
          lines = qt.get_lines()          
          lines.foreach(line => content += line)

          load = false
        }
      }
      
      onMouseClicked = (e: MouseEvent) => {
        val cr = new Circle{
              centerX = e.x
              centerY = e.y
              radius = rd
              stroke = Color.Red
            }
        
        qt.insert(Point(e.x,e.y))
        circles += cr
        
        content = circles
        lines = qt.get_lines()
        
        lines.foreach(line => content += line)                
      }
      
      onMouseDragged = (e: MouseEvent) => {
        for(i<-0 to nar){
          var xt = utils.random_between(0, sep)
          var yt = utils.random_between(0, sep)          
          
          if(qt.insert(Point(e.x+xt, e.y+yt))){
            val cr = new Circle{
              centerX = e.x+xt
              centerY = e.y+yt
              radius = rd
              stroke = Color.Red
            }
            
            circles += cr                   
          }
        }
        content = circles        
        lines = qt.get_lines()
        
        lines.foreach(line => content += line)
      }      
      
      onMouseMoved = (e: MouseEvent) => {
        val range:Box = Box(Point(e.x,e.y), Point(ran,ran))        
        matches = qt.range_query(range)
                
        var line1 = new Line{
          startX = e.x-ran
          startY = e.y-ran
          endX = e.x+ran 
          endY = e.y-ran
          stroke = Color.Blue
        }
        
        var line2 = new Line{
          startX = e.x+ran
          startY = e.y-ran
          endX = e.x+ran 
          endY = e.y+ran
          stroke = Color.Blue     
        }
        
        var line3 = new Line{
          startX = e.x+ran
          startY = e.y+ran
          endX = e.x-ran 
          endY = e.y+ran
          stroke = Color.Blue     
        }
        
        var line4 = new Line{
          startX = e.x-ran
          startY = e.y+ran
          endX = e.x-ran 
          endY = e.y-ran
          stroke = Color.Blue     
        }                  
                
        
        content = lines
        circles.foreach(pt => content += pt)
          
        matches.foreach(pt => content += pt)
        content += line1
        content += line2
        content += line3
        content += line4
        
      }
                
      // Parameters      
      fill = Color.Black
    }
  }
}