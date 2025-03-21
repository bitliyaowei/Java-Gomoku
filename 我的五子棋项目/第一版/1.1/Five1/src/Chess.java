import java.awt.*;
import java.awt.geom.Ellipse2D;

// 棋子类
public class Chess {
    private final Color color;
    private final int col;
    private final int row;
    private final ChessBoard cb;

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
        // 使用 ChessBoard 的 span 值来计算棋子的直径
        int diameter = cb.getSpan() - 2;
        // 棋子的中心坐标
        int xPos = col * cb.getSpan() + ChessBoard.MARGIN;
        int yPos = row * cb.getSpan() + ChessBoard.MARGIN;
        // 画棋子
        Graphics2D g2d = getGraphics2D((Graphics2D) g, xPos, yPos, diameter);
        // 使边界更均匀
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
        // 截取棋子大小的圆形，外切矩形的左上角坐标及矩形的长和宽
        Ellipse2D e = new Ellipse2D.Float(xPos - (float) diameter / 2, yPos - (float) diameter / 2, diameter, diameter);
        g2d.fill(e);
    }

    private Graphics2D getGraphics2D(Graphics2D g, int xPos, int yPos, int diameter) {
        // 棋子渐变
        RadialGradientPaint paint = null;
        // 棋子的高光点
        int x = xPos + diameter / 4;
        int y = yPos - diameter / 4;
        // 白黑渐变
        float[] f = {0f, 1f};
        Color[] c = {Color.WHITE, Color.BLACK};
        if (color == Color.black) {
            paint = new RadialGradientPaint(x, y, diameter, f, c); // 黑棋子
        } else if (color == Color.white) {
            paint = new RadialGradientPaint(x, y, diameter * 2, f, c); // 白棋子
        }
        g.setPaint(paint); // 设置画笔
        return g;
    }
}
