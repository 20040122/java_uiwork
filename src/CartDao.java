import java.sql.*;
import javax.swing.*;

class CartDao {
    private Connection connection;

    public CartDao(Connection connection) {
        this.connection = connection;
    }

    void addToCartDatabase(int productId, String productName, String timestamp) {
        try {
            String query = "INSERT INTO cart (product_id, product_name, tm) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            statement.setString(2, productName);
            statement.setTimestamp(3, Timestamp.valueOf(timestamp));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add product to cart database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void removeFromCartDatabase(int productId) {
        String query = "DELETE FROM cart WHERE product_id = ? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void clearCartDatabase() {
        String query = "DELETE FROM cart";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void resetCartId() {
        try {
            String query = "ALTER TABLE cart MODIFY COLUMN id INT AUTO_INCREMENT, AUTO_INCREMENT = 1";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to reset cart id.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}