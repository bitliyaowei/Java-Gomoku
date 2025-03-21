import java.awt.*;
import java.awt.geom.Ellipse2D;

// 棋子类
public class Chess {
    public static final int DIAMETER = ChessBoard.SPAN - 2;
    private final Color color;
    private final int col;
    private final int row;
    ChessBoard cb;

    public Chess(int col, int row, Color color, ChessBoard cb) {
        this.col = col;
        this.row = row;
        this.color = color;
        this.cb = cb;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Color getColor() {
        return color;
    }

    public void draw(Graphics g) {
        // 棋子的中心坐标
        int xPos = col * ChessBoard.SPAN + ChessBoard.MARGIN;
        int yPos = row * ChessBoard.SPAN + ChessBoard.MARGIN;
        // 画棋子
        Graphics2D g2d = getGraphics2D((Graphics2D) g, xPos, yPos);
        // 使边界更均匀
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
        // 截取棋子大小的圆形，外切矩形的左上角坐标及矩形的长和宽
        Ellipse2D e = new Ellipse2D.Float(xPos - (float) DIAMETER / 2, yPos - (float) DIAMETER / 2, DIAMETER, DIAMETER);
        g2d.fill(e);
    }

    private Graphics2D getGraphics2D(Graphics2D g, int xPos, int yPos) {
        // 棋子渐变
        RadialGradientPaint paint = null;
        // 棋子的高光点
        int x = xPos + DIAMETER / 4;
        int y = yPos - DIAMETER / 4;
        // 白黑渐变
        float[] f = {0f, 1f};
        Color[] c = {Color.WHITE, Color.BLACK};
        if (color == Color.black) {
            paint = new RadialGradientPaint(x, y, DIAMETER, f, c); // 黑棋子
        } else if (color == Color.white) {
            paint = new RadialGradientPaint(x, y, DIAMETER * 2, f, c); // 白棋子
        }
        g.setPaint(paint); // 设置画笔
        return g;
    }
}
