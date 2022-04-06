import javax.swing.{JFrame, SwingUtilities, WindowConstants}

@main def squaresMain: Unit =
  // Create the frame/panel on the event dispatching thread.
  SwingUtilities.invokeLater(
    new Runnable():
      def run: Unit = drawSquares
  )

def drawSquares: Unit =
  JFrame.setDefaultLookAndFeelDecorated(true)
  val frame = new JFrame("A chessboard of squares")
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  frame.setSize(600, 400)
  frame.add(SquaresPanel)
  frame.setVisible(true)