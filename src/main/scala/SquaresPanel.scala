import java.awt.{Color, Dimension, Graphics}
import javax.swing.*

object SquaresPanel extends JPanel:

  setBackground(Color.white)

  extension (p: Point)
    def deviceCoords(panelHeight: Int): (Int, Int) =
      (Math.round(p.x), panelHeight - Math.round(p.y))

  override def paintComponent(g: Graphics): Unit =

    super.paintComponent(g)

    val panelSize: Dimension = getSize()
    val panelWidth = panelSize.width - 1
    val panelHeight = panelSize.height - 1
    val panelCentre = Point(panelWidth / 2, panelHeight / 2)
    val gridSize = 8
    val squareSide: Float = 0.95F * Math.min(panelWidth, panelHeight) / gridSize
    val squareCount = 20

    enum Direction:
      case Left, Right
      def reversed: Direction = if this == Right then Left else Right

    def alternatingDirections(startDirection: Direction): LazyList[Direction] =
      LazyList.iterate(startDirection)(_.reversed)

    val mu: Float =
      val t = 3
      val x = Math.tan(t * (Math.PI/(4 * squareCount)))
      (x / (x + 1)).toFloat

    def shrinkAndTwist(direction: Direction): Square => Square =
      val q = if direction == Direction.Right then mu else 1-mu
      val p = 1 - q
      def combine(a: Point, b: Point) = Point(p * a.x + q * b.x, p * a.y + q * b.y)
      { case Square(a,b,c,d) => Square(combine(a,b), combine(b,c), combine(c,d), combine(d,a)) }

    def drawLine(a: Point, b: Point): Unit =
      val (ax,ay) = a.deviceCoords(panelHeight)
      val (bx,by) = b.deviceCoords(panelHeight)
      g.drawLine(ax, ay, bx, by)

    val draw: Square => Unit =
      case Square(a, b, c, d) =>
        drawLine(a, b)
        drawLine(b, c)
        drawLine(c, d)
        drawLine(d, a)

    def squareCentre(row: Int, col: Int): Point =
      Point(panelCentre.x - (gridSize / 2 * squareSide) + (col * squareSide) + squareSide / 2,
        panelCentre.y - (gridSize / 2 * squareSide) + (row * squareSide) + squareSide / 2)

    for
      (row, startDirection) <-
        (0 until gridSize) zip alternatingDirections(Direction.Right)
      (col, direction) <-
        (0 until gridSize) zip alternatingDirections(startDirection)
      square = Square(squareCentre(row,col), squareSide)
    yield LazyList
            .iterate(square)(shrinkAndTwist(direction))
            .take(squareCount+1)
            .foreach(draw)