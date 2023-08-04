package data;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelperDB {
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String password = System.getProperty("db.password");

    public static void clearTables() throws SQLException {
        String deleteOrderEntity = "DELETE FROM order_entity;";
        String deletePaymentEntity = "DELETE FROM payment_entity;";
        String deleteCreditRequestEntity = "DELETE FROM credit_request_entity;";
        QueryRunner runner = new QueryRunner();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            runner.update(conn, deleteOrderEntity);
            runner.update(conn, deletePaymentEntity);
            runner.update(conn, deleteCreditRequestEntity);

        }
    }

    public static String findPayStatus() throws SQLException {
        String statusSQL = "SELECT status FROM payment_entity;";
        return getData(statusSQL);
    }

    public static String findCreditStatus() throws SQLException {
        String statusSQL = "SELECT status FROM credit_request_entity;";
        return getData(statusSQL);
    }


    private static String getData(String query) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String data = "";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            data = runner.query(conn, query, new ScalarHandler<>());
        }
        return data;
    }

    public static long getOrderEntityCount() throws SQLException {
        String countSQL = "SELECT COUNT(*) FROM order_entity;";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            QueryRunner runner = new QueryRunner();
            Long count = runner.query(conn, countSQL, new ScalarHandler<>());
            return count != null ? count : 0;
        }
    }
}