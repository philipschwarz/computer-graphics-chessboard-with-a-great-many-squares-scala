case class Square(a: Point, b: Point, c: Point, d: Point)

object Square:

  def apply(centre: Point, side: Float): Square =
    val Point(x,y) = centre
    val halfSide = 0.5F * side
    val bottomLeft = Point(x - halfSide, y - halfSide)
    val bottomRight = Point(x + halfSide, y - halfSide)
    val topRight = Point(x + halfSide, y + halfSide)
    val topLeft = Point(x - halfSide, y + halfSide)
    Square(bottomLeft,bottomRight,topRight,topLeft)