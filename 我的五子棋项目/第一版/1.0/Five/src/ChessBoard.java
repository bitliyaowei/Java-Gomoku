import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// 棋盘
public class ChessBoard extends JPanel {
    public static final int ROWS = 18; // 行数
    public static final int COLS = 18; // 列数
    public static final int MARGIN = 35; // 边距
    public static final int SPAN = 30; // 当前网格宽度
    private static final int[][] SPECIAL_POINTS = {{3, 3}, {(COLS - 3), 3}, {COLS / 2, ROWS / 2}, {3, (ROWS - 3)}, {(COLS - 3), (ROWS - 3)}}; // 实心点坐标
    private static final int POINT_SIZE = 5; // 实心点长宽
    private static final String[] WALLPAPERS = {"/img/board1.png", "/img/board2.png"};
    private final List<Chess> chessList; // 记录已经下在棋盘上的棋子的数组
    private final Five five;
    int chessCount; // 当前棋盘上棋子的个数
    boolean isBlack = true; // 下一轮下的棋子颜色，默认先下黑棋
    boolean isGaming = true; // 是否正在游戏
    private Image img;
    private int wallpaperIndex = 0;

    public ChessBoard(Five five) {
        this.five = five;
        chessList = new ArrayList<>();
        changeWallpaper();
        this.addMouseListener(new MouseMonitor());
        this.addMouseMotionListener(new MouseMotionMonitor());
    }

    // 画棋盘
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
        // 画横线和竖线
        for (int i = 0; i <= ROWS; i++) {
            int y = MARGIN + i * SPAN;
            g.drawLine(MARGIN, y, MARGIN + COLS * SPAN, y);
            g.drawLine(MARGIN + i * SPAN, MARGIN, MARGIN + i * SPAN, MARGIN + ROWS * SPAN);
        }
        // 画五个点,参数为左上角点的x,y坐标及宽高
        for (int[] point : SPECIAL_POINTS) {
            g.fillRect(MARGIN + point[0] * SPAN - 2, MARGIN + point[1] * SPAN - 2, POINT_SIZE, POINT_SIZE);
        }

        for (Chess chess : chessList) {
            chess.draw(g);
            // 对当前下的棋子，则用红色矩形框出
            if (chess == chessList.get(chessCount - 1)) {
                int xPos = chess.getCol() * SPAN + MARGIN;
                int yPos = chess.getRow() * SPAN + MARGIN;
                g.setColor(Color.red);
                g.drawRect(xPos - Chess.DIAMETER / 2, yPos - Chess.DIAMETER / 2, Chess.DIAMETER, Chess.DIAMETER);
            }
        }

    }

    // 重新开始游戏
    public void restartGame() {
        // 清除棋子
        chessList.clear();
        // 重置变量
        isBlack = true;
        isGaming = true; // 游戏再开始
        chessCount = 0; // 当前棋盘棋子个数
        five.message.setText("请黑方下棋！");
        repaint();
    }

    // 悔棋
    public void goBack() {
        // 如果当前棋子数为0，则返回
        if (chessCount == 0) {
            return;
        }
        chessList.remove(chessCount - 1);
        chessCount--;
        isBlack = !isBlack;
        repaint();
        five.message.setText(String.format("请%s方下棋！", isBlack ? "黑" : "白"));
    }

    // 方法重载，判断是否已经有同色棋子了，如果颜色为null，则只判断是否有棋子
    private boolean hasChess(int col, int row, Color color) {
        for (Chess ch : chessList) {
            if (ch != null && ch.getCol() == col && ch.getRow() == row && (ch.getColor() == color || color == null)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否连五子棋
    private boolean isWin(int col, int row) {
        Color c = isBlack ? Color.BLACK : Color.WHITE;

        // 定义四个方向的增量
        int[][] directions = {{1, 0},  // 水平方向
                {0, 1},  // 垂直方向
                {1, 1},  // 左斜方向
                {1, -1}  // 右斜方向
        };

        for (int[] dir : directions) {
            if (checkDirection(col, row, c, dir)) {
                return true;
            }
        }

        return false;
    }

    // 检查当前位置的棋子是否满足连五子棋的要求
    private boolean checkDirection(int col, int row, Color c, int[] dir) {
        int continueCount = 1; // 包括当前位置的棋子
        // 可能会出现011_110这种情况，两边有棋子，但中间没有棋子，所以需要检查两边
        // 向 左/下/左上/左下 方向检查
        for (int i = 1; ; i++) {
            int x = col - i * dir[0];
            int y = row - i * dir[1];
            if (x >= 0 && y >= 0 && x <= COLS && y <= ROWS && hasChess(x, y, c)) {
                continueCount++;
            } else {
                break;
            }
        }
        // 向 右/上/右下/右上 方向检查
        for (int i = 1; ; i++) {
            int x = col + i * dir[0];
            int y = row + i * dir[1];
            if (x >= 0 && y >= 0 && x <= COLS && y <= ROWS && hasChess(x, y, c)) {
                continueCount++;
            } else {
                break;
            }
        }

        return continueCount >= 5;
    }


    // 返回棋盘尺寸
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MARGIN * 2 + COLS * SPAN, MARGIN * 2 + ROWS * SPAN);
    }

    public void setSizeOfPictures(String path) {
        URL imagePath = getClass().getResource(path);
        if (imagePath == null) {
            return;
        }
        ImageIcon icon = new ImageIcon(imagePath);
        img = icon.getImage().getScaledInstance(MARGIN * 2 + COLS * SPAN, MARGIN * 2 + ROWS * SPAN, Image.SCALE_SMOOTH);
    }


    public void changeWallpaper() {
        wallpaperIndex = (wallpaperIndex + 1) % WALLPAPERS.length;
        String path = WALLPAPERS[wallpaperIndex];
        setSizeOfPictures(path);
        repaint();
    }

    // 内部类，鼠标监听器, 鼠标移动事件
    class MouseMotionMonitor extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            int col = (e.getX() - MARGIN + SPAN / 2) / SPAN;
            int row = (e.getY() - MARGIN + SPAN / 2) / SPAN;
            if (col < 0 || col > COLS || row < 0 || row > ROWS || !isGaming || hasChess(col, row, null)) {
                ChessBoard.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                ChessBoard.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }
    }

    // 内部类，鼠标监听器, 鼠标点击事件
    class MouseMonitor extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            // 判断是否正在游戏
            if (!isGaming) {
                return;
            }
            // 将鼠标单击的像素坐标转换为棋盘坐标,该点是单元格的整数倍；
            int col = (e.getX() - MARGIN + SPAN / 2) / SPAN;
            int row = (e.getY() - MARGIN + SPAN / 2) / SPAN;
            // 判断鼠标点击的坐标可以下在棋盘上，并且该处没有棋子
            if (col < 0 || col > COLS || row < 0 || row > ROWS || hasChess(col, row, null)) {
                repaint();
                return;
            }
            // 创建棋子对象
            Chess tempChess = new Chess(col, row, isBlack ? Color.BLACK : Color.WHITE, ChessBoard.this);
            chessList.add(tempChess);
            chessCount++;
            repaint(); // 刷新棋盘
            if (isWin(col, row)) {
                String colorName = isBlack ? "黑棋" : "白棋";
                String msg = String.format("恭喜，%s赢了！", colorName);
                five.message.setText(msg);// 显示消息
                JOptionPane.showMessageDialog(ChessBoard.this, msg); // 弹出对话框
                isGaming = false;
                return;
            }
            isBlack = !isBlack; // 下一轮的棋子颜色
            five.message.setText(String.format("请%s方下棋！", isBlack ? "黑" : "白"));
        }
    }
}
