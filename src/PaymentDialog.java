import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentDialog extends JDialog {
    private JLabel qrCodeLabel;

    public PaymentDialog(JFrame parentFrame) {
        super(parentFrame, "支付方式选择", true);

        // 创建组件
        JButton weChatButton = new JButton("微信");
        JButton alipayButton = new JButton("支付宝");
        qrCodeLabel = new JLabel();

        // 设置字体
        Font buttonFont = new Font("Microsoft YaHei", Font.BOLD, 20);
        weChatButton.setFont(buttonFont);
        alipayButton.setFont(buttonFont);


        // 设置按钮属性
        Dimension buttonSize = new Dimension(200 , 100);
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
            }
        });

        alipayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon alipayQRCode = new ImageIcon("src/Alipay.png");
                qrCodeLabel.setIcon(alipayQRCode);
            }
        });

        // 创建布局
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(weChatButton);
        buttonPanel.add(alipayButton);
        mainPanel.add(qrCodeLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // 设置窗口属性
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(parentFrame);
        setContentPane(mainPanel);
        setVisible(true);
    }
}
