package snake;

import java.awt.Point;

public class TetrisPoint {
    protected Point point;
    protected String type;

    public TetrisPoint(int x, int y, String type) {
        this.point = new Point(x, y);
        this.type = type;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public void setX(int x) {
        point.x = x;
    }

    public void setY(int y) {
        point.y = y;
    }

    public String getType() {
        return type;
    }

}
