package com.mycompany.imagej;

import javax.swing.*;
import java.awt.*;

// 等待界面的类
public class WaitingDialog extends JDialog {
    private JLabel statusLabel;

    public WaitingDialog(Frame parent) {
        super(parent, "请稍候", true);
        statusLabel = new JLabel("正在加载...");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setVerticalAlignment(SwingConstants.CENTER);
        this.add(statusLabel);
        this.setSize(300, 150);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // 更新状态文本的方法
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }
    public static void main(String[] args) {
        // 创建主窗口
        JFrame mainFrame = new JFrame("主程序窗口");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        // 创建等待对话框实例
        WaitingDialog waitingDialog = new WaitingDialog(mainFrame);

        // 显示主窗口
        mainFrame.setVisible(true);

        // 在需要显示等待界面时调用
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                waitingDialog.setVisible(true);
            }
        });

        // 模拟长时间运行的任务
        try {
            Thread.sleep(5000); // 5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 任务完成后关闭等待界面
        waitingDialog.setVisible(false);
    }
}

// 主程序类
