package data;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelperDB {
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String password = System.getProperty("db.password");

    public static void clearTables() {
        String deleteOrderEntity = "DELETE FROM order_entity;";
        String deletePaymentEntity = "DELETE FROM payment_entity;";
        String deleteCreditRequestEntity = "DELETE FROM credit_request_entity;";
        QueryRunner runner = new QueryRunner();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            runner.update(conn, deleteOrderEntity);
            runner.update(conn, deletePaymentEntity);
            runner.update(conn, deleteCreditRequestEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String findPayStatus() {
        String statusSQL = "SELECT status FROM payment_entity;";
        try {
            return getData(statusSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            return "ОШИБКА: Не удалось получить статус покупки из базы данных.";
        }
    }

    public static String findCreditStatus() {
        String statusSQL = "SELECT status FROM credit_request_entity;";
        try {
            return getData(statusSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            return "ОШИБКА: Не удалось получить статус покупки в кредит из базы данных.";
        }
    }


    private static String getData(String query) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String data = "";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            data = runner.query(conn, query, new ScalarHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static long getOrderEntityCount() {
        String countSQL = "SELECT COUNT(*) FROM order_entity;";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            QueryRunner runner = new QueryRunner();
            Long count = runner.query(conn, countSQL, new ScalarHandler<>());
            return count != null ? count : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getPaymentEntityCount() {
        return getTableCount("SELECT COUNT(*) FROM payment_entity;");
    }

    public static long getCreditRequestEntityCount() {
        return getTableCount("SELECT COUNT(*) FROM credit_request_entity;");
    }

    private static long getTableCount(String countSQL) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            QueryRunner runner = new QueryRunner();
            Long count = runner.query(conn, countSQL, new ScalarHandler<>());
            return count != null ? count : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Проверяет, что данные карты не сохраняются в СУБД (требование ТЗ)
    public static boolean cardNumberStoredInDb(String cardNumber) {
        String likePattern = "%" + cardNumber + "%";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            QueryRunner runner = new QueryRunner();
            Long payments = runner.query(conn,
                    "SELECT COUNT(*) FROM payment_entity WHERE carddata LIKE ?;",
                    new ScalarHandler<>(), likePattern);
            Long credits = runner.query(conn,
                    "SELECT COUNT(*) FROM credit_request_entity WHERE carddata LIKE ?;",
                    new ScalarHandler<>(), likePattern);
            return (payments != null && payments > 0) || (credits != null && credits > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}