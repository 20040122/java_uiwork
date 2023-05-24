import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ECommerceApp extends JFrame {
    private List<Product> productList;
    private JList<Product> productJList;
    private DefaultListModel<Product> cartListModel;
    private Connection connection;

    private CartDao cartDao;
    public ECommerceApp() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 初始化数据库连接
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce", "root", "123456");
            cartDao = new CartDao(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }


        // 初始化商品列表
        productList = new ArrayList<>();
        initializeProductList();


        // 创建商品列表组件
        productJList = new JList<>(new DefaultListModel<>());
        for (Product product : productList) {
            ((DefaultListModel<Product>) productJList.getModel()).addElement(product);
        }
        productJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        productJList.setFont(new Font("Microsoft YaHei", Font.BOLD, 17));


        // 创建购物车列表组件
        cartListModel = new DefaultListModel<>();
        JList<Product> cartJList = new JList<>(cartListModel);
        cartJList.setFont(new Font("Microsoft YaHei", Font.BOLD, 17));


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 生成当前日期时间的字符串
        String tmValue = dateFormat.format(new Date());
        // 创建按钮和事件处理程序


        JButton addButton = new JButton("添加至购物车");
        customizeButton(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Product> selectedProducts = productJList.getSelectedValuesList();
                for (Product product : selectedProducts) {
                    cartListModel.addElement(product);
                    cartDao.addToCartDatabase(product.getId(), product.getName(),tmValue);
                }
            }
        });


        JButton removeButton = new JButton("移除购物车");
        customizeButton(removeButton);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Product> selectedProducts = cartJList.getSelectedValuesList();
                for (Product product : selectedProducts) {
                    cartListModel.removeElement(product);
                    cartDao.removeFromCartDatabase(product.getId());
                }
            }
        });


        JButton checkoutSelectedButton = new JButton("结算");
        customizeButton(checkoutSelectedButton);
        checkoutSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Product> selectedProducts = cartJList.getSelectedValuesList();
                if (selectedProducts.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No selected products.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    double total = 0;
                    for (Product product : selectedProducts) {
                        total += product.getPrice();
                    }
                    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                    String formattedTotal = decimalFormat.format(total);
                    JOptionPane.showMessageDialog(null, "Total: $" + formattedTotal, "Checkout", JOptionPane.INFORMATION_MESSAGE);
                    for (Product product : selectedProducts) {
                        cartListModel.removeElement(product);
                        cartDao.removeFromCartDatabase(product.getId());
                        cartDao.resetCartId();
                    }
                }
            }
        });



        // 创建布局
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        productPanel.add(new JLabel("商品列表", SwingConstants.CENTER), BorderLayout.NORTH);
        productPanel.add(new JScrollPane(productJList), BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(checkoutSelectedButton);


        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cartPanel.add(new JLabel("购物车列表", SwingConstants.CENTER), BorderLayout.NORTH);
        cartPanel.add(new JScrollPane(cartJList), BorderLayout.CENTER);
        cartPanel.add(buttonPanel, BorderLayout.SOUTH);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(productPanel, BorderLayout.WEST);
        mainPanel.add(cartPanel, BorderLayout.CENTER);


        // 设置窗口属性
        setTitle("仿电商平台购物车");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 添加窗口关闭事件处理程序
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cartDao.clearCartDatabase();
                cartDao.resetCartId();
            }
        });
    }


    private void customizeButton(JButton button) {
        button.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        button.setBackground(new Color(0, 0, 0));
        button.setForeground(Color.RED);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }


    private void initializeProductList() {
        // 从CSV文件读取商品数据
        try (BufferedReader reader = new BufferedReader(new FileReader("src/data.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                double price = Double.parseDouble(data[2].trim());
                productList.add(new Product(id, name, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ECommerceApp();
            }
        });
    }
}



