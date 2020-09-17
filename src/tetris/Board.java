package tetris;

import javax.swing.JPanel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Board extends JPanel {
    public static final int SCALE = 30;
    public static final int WIDTH = 12;
    public static final int HEIGHT = 20;
    private Map<String, Color> colorMap;
    private static final Color background = Color.black;
    private  String[][] boardColor;


    public Board() {
        colorMap = new HashMap<>();
        colorMap.put("line", new Color(51,204, 255));
        colorMap.put("lShape", new Color(0,153, 51));
        colorMap.put("square", new Color(255,102, 102));
        colorMap.put("middle", new Color(167, 93, 240));
        colorMap.put("Zshape", new Color(237, 130, 214));
        colorMap.put("iZshape", new Color(238, 189, 10, 215));
        colorMap.put("none", background);
        boardColor = new String [WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                boardColor[i][j] = "none";
            }
        }
    }


    // Returns true if the current point on the board is occupied
    public boolean touched (TetrisPoint point) {
        return boardColor[point.getX()][point.getY()] != "none";
    }


    // Clears all tetrominos from the game board
    public void boardClear() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                boardColor[j][i] = "none";
            }
        }
    }


    // Returns the x-coordinate of the left most square in the given tetromino
    public int mostLeft(TetrisPoint[] points) {
        int result = points[0].getX();
        for (TetrisPoint point : points) {
            result = Math.min(result, point.getX());
        }

        return result;
    }


    // Returns the x-coordinate of the right most square in the given tetromino
    public int mostRight(TetrisPoint[] points) {
        int result = points[0].getX();
        for (TetrisPoint point : points) {
            result = Math.max(result, point.getX());
        }

        return result;
    }

    // Returns the y-coordinate of the bottom most square in the given tetromino
    public int mostBottom(TetrisPoint[] points) {
        int result = points[0].getY();
        for (TetrisPoint point : points) {
            result = Math.max(result, point.getY());
        }
        return result;
    }


    // Adjust the positions of the tetromino after rotation to prevent any overlap of squares
    public void rotatedPoints(TetrisPoint[] points) {
        if (mostBottom(points) >= Board.HEIGHT) {
            int difference = mostBottom(points) - Board.HEIGHT + 1;
            for (TetrisPoint point : points) {
                point.setY(point.getY() - difference);
            }
        }

        if (mostLeft(points) < 0) {
            int difference = 0 - mostLeft(points);
            for (TetrisPoint point : points) {
                point.setX(point.getX() + difference);
            }
        }

        if (mostRight(points) >= Board.WIDTH) {
            int difference = mostRight(points) - Board.WIDTH + 1;
            for (TetrisPoint point : points) {
                point.setX(point.getX() - difference);
            }
        }

        boolean valid = true;
        for (TetrisPoint point : points) {
            if (point.getY() >= 0 && touched(point)) {
                valid = false;
                break;
            }
        }
        if (valid) {
            return;
        }
        // try shift left
        TetrisPoint[] newpoints = new TetrisPoint[4];
        for (int i = 0; i < 4; i++) {
            newpoints[i] = new TetrisPoint(points[i].getX() - 1, points[i].getY(), points[i].getType());
        }
        boolean touch = false;
        for (TetrisPoint point : newpoints) {
            if (point.getX() < 0 || (point.getY() >= 0 &&touched(point))) {
                touch = true;
                break;
            }
        }
        if (!touch) {
            for (TetrisPoint point : points) {
                point.setX(point.getX() - 1);
            }
            return;
        }

        // try shift right
        for (int i = 0; i < 4; i++) {
            newpoints[i] = new TetrisPoint(points[i].getX() + 1, points[i].getY(), points[i].getType());
        }
        touch = false;
        for (TetrisPoint point : newpoints) {
            if (point.getX() >= Board.WIDTH || (point.getY() >= 0 && touched(point))) {
                touch = true;
                break;
            }
        }
        if (!touch) {
            for (TetrisPoint point : points) {
                point.setX(point.getX() + 1);
            }
            return;
        }

        if (points[0].getType().equals("line")) { // try left and then right again
            for (int i = 0; i < 4; i++) {
                newpoints[i] = new TetrisPoint(points[i].getX() - 2, points[i].getY(), points[i].getType());
            }
            touch = false;
            for (TetrisPoint point : newpoints) {
                if (point.getX() < 0 || (point.getY() >= 0 && touched(point))) {
                    touch = true;
                    break;
                }
            }
            if (!touch) {
                for (TetrisPoint point : points) {
                    point.setX(point.getX() - 2);
                }
                return;
            }
            for (int i = 0; i < 4; i++) {
                newpoints[i] = new TetrisPoint(points[i].getX() + 2, points[i].getY(), points[i].getType());
            }
            touch = false;
            for (TetrisPoint point : newpoints) {
                if (point.getX() >= Board.WIDTH || (point.getY() >= 0 && touched(point))) {
                    touch = true;
                    break;
                }
            }
            if (!touch) {
                for (TetrisPoint point : points) {
                    point.setX(point.getX() + 2);
                }
                return;
            }
        }

         // shift up
        int shift = 0;
        for (int i = 0; i < 4; i++) {
            newpoints[i] = new TetrisPoint(points[i].getX(), points[i].getY() - 1, points[i].getType());
        }
        for (TetrisPoint point : newpoints) {
            int x = point.getX();
            int y = point.getY();
            int curShift = 1;
            int newY = y;
            while (newY >= 0 && boardColor[x][newY] != "none") {
                curShift++;
                newY--;
            }
            shift = Math.max(shift, curShift);
        }
        for (TetrisPoint point : points) {
            point.setY(point.getY() - shift );
        }
    }


    // Determines if the given tetromino has reached the bottom of the board
    public boolean reachBottom(TetrisPoint[] tetris) {

        for (TetrisPoint t : tetris) {
            int x = t.getX();
            int y = t.getY();

            if (y < 0) {
                return false;
            }

            if ( y >= Board.HEIGHT - 1 || boardColor[x][y+1] != "none") {
                return true;
            }
        }
        return false;
    }


    // Determines if the given tetromino has touched the board's left border
    public boolean touchLeftBorder(TetrisPoint[] tetris) {
        for (TetrisPoint t : tetris) {
            int x = t.getX();
            int y = t.getY();

            if (x <= 0 || boardColor[x-1][y] != "none") {
                return true;
            }
        }
        return false;
    }

    // Determines if the given tetromino has touched the board's right border
    public boolean touchRightBorder(TetrisPoint[] tetris) {
        for (TetrisPoint t : tetris) {
            int x = t.getX();
            int y = t.getY();

            if (x >= Board.WIDTH - 1 || boardColor[x+1][y] != "none") {
                return true;
            }
        }
        return false;
    }

    public boolean isGameOver() {
        for (int i = 0; i < WIDTH; i++) {
            if (boardColor[i][0] != "none") {
                return true;
            }
        }
        return false;
   }

     // Updates the display of the game board
    public void updateBoardColor() {
        TetrisPoint[] curTetris = Game.getTetris();
        for (TetrisPoint t : curTetris) {
            int x = t.getX();
            int y = t.getY();
            if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                boardColor[x][y] = t.getType();
            }
        }

        for (int h = 0; h < HEIGHT; h++) {
            boolean filled = true;
            for (int w = 0; w < WIDTH; w++) {
                if (boardColor[w][h] == "none") {
                    filled = false;
                    break;
                }
            }
            if (filled) {
                Game.setScore(Game.getScore() + 1);
                for (int i = 0; i < WIDTH; i++) {
                    boardColor[i][h] = "none";
                }

                for (int j = h; j > 0; j--) {
                    for (int z = 0; z < WIDTH; z++) {
                        boardColor[z][j] = boardColor[z][j - 1];
                    }
                }
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(background);
        g.fillRect(0 ,0, WIDTH * SCALE, HEIGHT * SCALE);

        if (Game.getGameOver()) {
            g.setColor(Color.white);
            g.drawString("Game Over", 10, 10);
            g.drawString("Your final score is: " + Game.getScore(), 10, 30);
            return;
        }

        g.setColor(Color.white);
        g.drawString("Your Score: " + Game.getScore(), 10, 10);


        TetrisPoint[] tetris = Game.getTetris();
        if (tetris != null) {
            for (TetrisPoint t : tetris) {
                g.setColor(colorMap.get(t.getType()));
                g.fillRect(t.getX() * SCALE, t.getY() * SCALE, SCALE, SCALE);
            }
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                String c = boardColor[i][j];
                if (c != "none") {
                    g.setColor(colorMap.get(c));
                    g.fillRect(i * SCALE, j * SCALE, SCALE, SCALE);
                }
            }
        }
    }
}
