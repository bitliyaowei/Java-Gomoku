import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 创建一个名为 Five 的类，继承自 JFrame 类，并实现了一个五子棋游戏。
public class Five extends JFrame {
    private final ChessBoard boardPanel;
    JLabel message;

    public Five() {
        super("单机版五子棋");
        setLayout(new BorderLayout()); // 设置窗体的布局管理器为边界布局

        JToolBar toolbar = new JToolBar(); // 创建工具栏
        JButton startButton = new JButton("重新开始");
        JButton backButton = new JButton("悔棋");
        JButton changeWallpaperButton = new JButton("更换棋桌");
        JButton exitButton = new JButton("退出");

        message = new JLabel("请黑方下棋！");
        boardPanel = new ChessBoard(this);

        toolbar.add(startButton);
        toolbar.add(backButton);
        toolbar.add(changeWallpaperButton);
        toolbar.add(exitButton);

        add(toolbar, BorderLayout.NORTH); // 将工具栏添加到窗体的北边
        add(boardPanel, BorderLayout.CENTER); // 将棋盘面板添加到窗体的中心位置
        add(message, BorderLayout.SOUTH); // 将提示标签添加到窗体的南边

        setLocation(400, 100); // 设置窗体的位置
        setSize(600, 600);
        pack(); // 调用 pack() 方法，根据组件的首选大小来调整窗体的大小
        setResizable(false); // 禁止用户调整窗体大小
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 设置窗体关闭操作
        setVisible(true); // 显示窗体

        ActionMonitor monitor = new ActionMonitor();
        startButton.addActionListener(monitor);
        backButton.addActionListener(monitor);
        exitButton.addActionListener(monitor);
        changeWallpaperButton.addActionListener(monitor);
    }

    public static void main(String[] args) {
        new Five();
    }

    // 创建一个内部监听类，用于处理工具栏按钮的事件。
    class ActionMonitor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = ((JButton) e.getSource()).getText();
            switch (actionCommand) {
                case "重新开始":
                    boardPanel.restartGame();
                    break;
                case "悔棋":
                    boardPanel.goBack();
                    break;
                case "更换棋桌":
                    boardPanel.changeWallpaper();
                    break;
                case "退出":
                    System.exit(0);
                    break;
            }
        }
    }

}
