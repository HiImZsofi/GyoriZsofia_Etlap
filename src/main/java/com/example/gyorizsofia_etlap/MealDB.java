package com.example.gyorizsofia_etlap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDB {
    private Connection conn;
    public static String DB_DRIVER = "mysql";
    public static String DB_HOST = "localhost";
    public static String DB_PORT = "3306";
    public static String DB_NAME = "etlapdb";
    public static String DB_USERNAME = "root";
    public static String DB_PASSWORD = "";

    public MealDB() throws SQLException {
        String url = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_NAME);
        conn = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD);
    }

    public boolean createMeal(Meal meal) throws SQLException {
        String sql = "INSERT INTO etlap(nev, leiras, ar, kategoria) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, meal.getName());
        stmt.setString(2, meal.getDescription());
        stmt.setInt(3, meal.getPrice());
        stmt.setString(4, meal.getCategory());
        return stmt.executeUpdate() > 0;
    }

    public List<Meal> readMeals() throws SQLException {
        List<Meal> etelek = new ArrayList<>();
        String sql = "SELECT * FROM etlap";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        while(result.next()) {
            int id = result.getInt("id");
            String nev = result.getString("nev");
            String leiras = result.getString("leiras");
            int ar = result.getInt("ar");
            String kategoria = result.getString("kategoria");
            Meal etel = new Meal(id, nev, leiras, ar, kategoria);
            etelek.add(etel);
        }
        return etelek;
    }

    public boolean updateMealPricePercentage(Meal etel, double szazalek) throws SQLException {
        String sql = "UPDATE etlap SET ar= ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, (int) (etel.getPrice() * szazalek));
        stmt.setInt(2, etel.getId());
        return stmt.executeUpdate() > 0;
    }

    public boolean updateMealPrice(Meal etel, int emeles) throws SQLException {
        String sql = "UPDATE etlap SET ar= ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, etel.getPrice() + emeles);
        stmt.setInt(2, etel.getId());
        return stmt.executeUpdate() > 0;
    }

    public boolean deleteMeal(int id) throws SQLException {
        String sql = "DELETE FROM etlap WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        return stmt.executeUpdate() > 0;
    }
}