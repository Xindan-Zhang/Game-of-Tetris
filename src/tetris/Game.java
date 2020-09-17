package tetris;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.Timer;

public class Game extends JFrame implements ActionListener, KeyListener {
    private JFrame game;
    private Board board;
    private static TetrisPoint[] tetris;
    private Random random = new Random();
    private Timer timer;
    private boolean needNewTetris = false;
    private static boolean gameOver = false;
    private final int numShape = 6;
    private static int score;
    private boolean pause = false;



    public Game() {
        game = new JFrame("Tetris");
        game.setVisible(true);
        game.setSize(500, 600);
        game.setLocationRelativeTo(null);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board = new Board();
        game.add(board);
        generateTetris();
        score = 0;
        game.addKeyListener(this);
        timer = new Timer(500, this);
        timer.start();

    }


    public static boolean getGameOver() {
        return gameOver;
    }


    // Randomly generates a new tetromino
    private void generateTetris() {
        int choice = random.nextInt(numShape);
        tetris = new TetrisPoint[4];

        switch(choice) {
            case 0: // ----
                for (int i = 0; i < 4; i++) {
                    tetris[i] = new TetrisPoint(i + 4, 0, "line");
                }
                break;

            case 1: // L
                for (int i = 0; i < 3; i++) {
                    tetris[i] = new TetrisPoint(5, i, "lShape");
                }
                tetris[3] = new TetrisPoint(6, 2, "lShape");
                break;

            case 2: // square
                tetris[0] = new TetrisPoint(5, 0, "square");
                tetris[1] = new TetrisPoint(5, 1, "square");
                tetris[2] = new TetrisPoint(6, 0, "square");
                tetris[3] = new TetrisPoint(6, 1, "square");
                break;

            case 3: // middle
                tetris[0] = new TetrisPoint(5, 1, "middle");
                tetris[1] = new TetrisPoint(6, 1, "middle");
                tetris[2] = new TetrisPoint(6, 0, "middle");
                tetris[3] = new TetrisPoint(7, 1, "middle");
                break;

            case 4: // Z
                tetris[0] = new TetrisPoint(5, 0, "Zshape");
                tetris[1] = new TetrisPoint(6, 1, "Zshape");
                tetris[2] = new TetrisPoint(6, 0, "Zshape");
                tetris[3] = new TetrisPoint(7, 1, "Zshape");
                break;

            case 5: // inverted Z
                tetris[0] = new TetrisPoint(5, 1, "iZshape");
                tetris[1] = new TetrisPoint(6, 1, "iZshape");
                tetris[2] = new TetrisPoint(6, 0, "iZshape");
                tetris[3] = new TetrisPoint(7, 0, "iZshape");
        }
    }

    // Rotate tetris 90 degrees counterclockwise
    public void rotateTetris() {
        if (tetris[0].getType() == "square") {
            return;
        }

        TetrisPoint[] newTetris = new TetrisPoint[4];
        int pivotX = tetris[1].getX();
        int pivotY = tetris[1].getY();

        for (int i = 0; i < 4; i++) {
            newTetris[i] = new TetrisPoint( pivotY - tetris[i].getY() + pivotX,
                    tetris[i].getX() - pivotX + pivotY, tetris[i].getType());
        }

        board.rotatedPoints(newTetris);
        tetris = newTetris;
    }


    public static TetrisPoint[] getTetris() {
        return tetris;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Game.score = score;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!pause) {

            if (!board.reachBottom(tetris)) {
                for (TetrisPoint t : tetris) {
                    t.setY(t.getY() + 1);
                }
            } else {
                needNewTetris = true;
            }

            if (needNewTetris) {
                board.updateBoardColor();
                gameOver = board.isGameOver();

                if (!gameOver) {
                    generateTetris();
                    needNewTetris = false;
                }
            }
            game.repaint();
        }
    }


    // Drops the current tetromino to the bottom of the board
    private void goToBottom() {
        while (!board.reachBottom(tetris)) {
            for (TetrisPoint point : tetris) {
                point.setY(point.getY() + 1);
            }
        }
    }

    // Restarts the game
    private void restart() {
        board.boardClear();
        generateTetris();
        score = 0;
        gameOver = false;
        needNewTetris = false;
        pause = false;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && !board.touchLeftBorder(tetris)) {
            for (TetrisPoint t : tetris) {
                t.setX(t.getX() - 1);
            }
            game.repaint();

        } else if (key == KeyEvent.VK_RIGHT && !board.touchRightBorder(tetris)) {
            for (TetrisPoint t : tetris) {
                t.setX(t.getX() + 1);
            }
            game.repaint();

        } else if (key == KeyEvent.VK_UP) {
            rotateTetris();
            game.repaint();

        } else if (key == KeyEvent.VK_DOWN) {
            goToBottom();
            game.repaint();

        } else if (key == KeyEvent.VK_S) {
            restart();
            game.repaint();

        } else if (key == KeyEvent.VK_SPACE) {
            pause = !pause;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
