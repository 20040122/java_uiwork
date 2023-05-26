import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class PaymentDialog extends JDialog {
    private JLabel qrCodeLabel;
    private JLabel countdownLabel;
    private Timer timer;
    private double remainingSeconds;

    public PaymentDialog(JFrame parentFrame) {
        super(parentFrame, "支付方式选择", true);


        // 创建组件
        JButton weChatButton = new JButton("微信");
        JButton alipayButton = new JButton("支付宝");
        qrCodeLabel = new JLabel();
        countdownLabel = new JLabel();


        // 设置字体
        Font buttonFont = new Font("Microsoft YaHei", Font.BOLD, 20);
        weChatButton.setFont(buttonFont);
        alipayButton.setFont(buttonFont);
        countdownLabel.setFont(buttonFont);

        // 设置按钮属性
        Dimension buttonSize = new Dimension(200, 100);
        weChatButton.setPreferredSize(buttonSize);
        alipayButton.setPreferredSize(buttonSize);
        weChatButton.setHorizontalAlignment(SwingConstants.CENTER);
        alipayButton.setHorizontalAlignment(SwingConstants.CENTER);

        // 添加按钮点击事件监听器
        weChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon weChatQRCode = new ImageIcon("src/wechat.png");
                qrCodeLabel.setIcon(weChatQRCode);
                remainingSeconds = 20.00; // 设置倒计时秒数
                countdownLabel.setText("剩余秒数：" + formatSeconds(remainingSeconds));
                countdownLabel.setForeground(Color.BLACK);
                // 设置定时器，延时10秒后自动关闭窗口
                timer = new Timer(10, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        remainingSeconds-=0.01;
                        countdownLabel.setText("剩余支付时间：" + formatSeconds(remainingSeconds));
                        if (remainingSeconds <= 0) {
                            dispose(); // 关闭支付窗口
                            new PaymentDialog(parentFrame);
                        }
                    }
                });
                timer.setRepeats(true); // 设置定时器仅触发一次
                timer.start(); // 启动定时器
            }
        });

        alipayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon alipayQRCode = new ImageIcon("src/Alipay.png");
                qrCodeLabel.setIcon(alipayQRCode);
                remainingSeconds = 20.00; // 设置倒计时秒数
                countdownLabel.setText("剩余秒数：" + formatSeconds(remainingSeconds));
                countdownLabel.setForeground(Color.BLACK);
                // 设置定时器，延时10秒后自动关闭窗口
                timer = new Timer(10, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        remainingSeconds-=0.01;
                        countdownLabel.setText("剩余支付时间：" + formatSeconds(remainingSeconds));
                        if (remainingSeconds <= 0) {
                            dispose(); // 关闭支付窗口
                            new PaymentDialog(parentFrame);
                        }
                    }
                });
                timer.setRepeats(true); // 设置定时器仅触发一次
                timer.start(); // 启动定时器
            }
        });

        // 创建布局
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(weChatButton);
        buttonPanel.add(alipayButton);
        mainPanel.add(qrCodeLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(countdownLabel, BorderLayout.EAST);


        // 设置窗口属性
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(parentFrame);
        setContentPane(mainPanel);
        setVisible(true);
    }
    private String formatSeconds(double seconds) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(seconds);
    }
    @Override
    public void dispose() {
        if (timer != null) {
            timer.stop(); // 停止定时器
        }
        super.dispose();
    }
}
