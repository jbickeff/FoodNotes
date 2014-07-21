package javadatabasev0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DB {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static String DB_URL = "jdbc:mysql://localhost/foodnotes";
    //  Database credentials
    static String USER = "root";
    static String PASS = "";

    public DB() {
        String path = System.getenv("OPENSHIFT_DATA_DIR");

        // if it's null it's NOT on openshift
        if (path != null) {
            USER = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
            PASS = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");

            String host = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
            String port = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
            String name = "tomcat";

            //DB_URL = "jdbc:" + System.getenv("OPENSHIFT_MYSQL_DB_URL");
            DB_URL = "jdbc:mysql://" + host + ":" + port + "/" + name;
        }

    }

    public String getUserName(String pUId) throws Exception {
        String theUName;
        Statement stmt = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;

            sql = "SELECT userName FROM users WHERE id='" + pUId + "'";

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            theUName = rs.getString("userName");
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        }

        return theUName;
    }

    String getUserId(String userName, String userPass) throws Exception {
        String userId = "";
        Statement stmt = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;

            sql = "SELECT id FROM users WHERE userName='"
                    + userName + "' && password='" + userPass + "'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            userId = rs.getString("id");
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
        return userId;
    }

    List<Entry> getEntries(String userId) throws Exception {
        List<Entry> theEntries = new ArrayList();

        Statement stmt = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, date, comments FROM entries WHERE userId=" + userId;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("id");
                String date = rs.getString("date");
                String comments = rs.getString("comments");

                Entry temp = new Entry(id, userId, date, comments);

                theEntries.add(temp);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            throw e;

        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        }

        return theEntries;
    }

    Boolean addEntry(List<String> pIngredients,
            List<String> pSymptoms, String pDate,
            String pComments, String userId) throws Exception {
        Boolean additionComplete = false;
        Boolean entryAdded = false;

        PreparedStatement stmt = null;
        Connection conn = null;
        String entryId = "";
        ResultSet generatedKeys = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sql;
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date preDate = formatter.parse(pDate);
            java.sql.Date theDate = new java.sql.Date(preDate.getTime());

            sql = "INSERT INTO entries (date, userId, comments) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, theDate);
            stmt.setInt(2, Integer.parseInt(userId));
            stmt.setString(3, pComments);
            int numberOfRows = stmt.executeUpdate();
            int newId;

            if (numberOfRows != 1) {
                throw new SQLException("Inserting entries effected " + numberOfRows + "when it should have only effected 1");
            } else {
                generatedKeys = stmt.getGeneratedKeys();
                generatedKeys.next();
                entryId = "" + generatedKeys.getInt(1);

                entryAdded = true;
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (generatedKeys != null) {
                generatedKeys.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }

        }

        if (entryAdded
                && addIngredients(pIngredients, entryId)
                && addSymptoms(pSymptoms, entryId)) {
            additionComplete = true;
        }

        return additionComplete;
    }

    Boolean addIngredients(List<String> pIngredients, String entryId) throws Exception {
        Boolean ingredientsAdded = false;
        PreparedStatement stmt = null;
        Connection conn = null;
        List<String> ingredientIds = new ArrayList();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sql;

            for (String ingredient : pIngredients) {
                ingredient = ingredient.toLowerCase();
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                sql = "SELECT id, name FROM ingredients WHERE Name=?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, ingredient);
                ResultSet rs = stmt.executeQuery();

                //if no ingredient is returned then it's not in the list.
                if (!rs.isBeforeFirst()) {

                    rs.next();
                    sql = "INSERT INTO ingredients (NAME) VALUES ('"
                            + ingredient + "')";
                    stmt.executeUpdate(sql);

                    sql = "SELECT id FROM ingredients WHERE Name='" + ingredient + "'";
                    rs = stmt.executeQuery(sql);
                    //ingredientIds.add(rs.getString("id"));
                }

                rs.next();
                ingredientIds.add(rs.getString("id"));

                stmt.close();
                conn.close();
            }
            
            ingredientsAdded = true;
            updateIngredientId(ingredientIds, entryId);

        } catch (Exception e) {
            throw e;

        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
        return ingredientsAdded;
    }

    void updateIngredientId(List<String> pIds, String entryId) throws Exception {
        Statement stmt = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sql;
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            for (String i : pIds) {
                stmt = conn.createStatement();

                //System.out.println("Ingredient id: " + i);
                sql = "INSERT INTO ingredientid (ingredientId, entryId)"
                        + "VALUES (" + i + "," + entryId + ")";
                stmt.executeUpdate(sql);
                stmt.close();

            }
            conn.close();

        } catch (Exception e) {
            throw e;

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    Boolean addSymptoms(List<String> pSymptoms, String entryId) throws Exception {
        Boolean symptomsAdded = false;
        Statement stmt = null;
        Connection conn = null;
        List<String> symptomIds = new ArrayList();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sql;
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            for (String symptom : pSymptoms) {
                symptom = symptom.toLowerCase();
                sql = "SELECT id, name FROM symptoms WHERE name='"
                        + symptom + "'";

                ResultSet rs = stmt.executeQuery(sql);

                if (!rs.next()) {
                    sql = "INSERT INTO symptoms (name) VALUES ('"
                            + symptom + "')";
                    stmt.executeUpdate(sql);

                    sql = "SELECT id FROM symptoms WHERE name='" + symptom + "'";

                    rs = stmt.executeQuery(sql);
                    rs.next();
                }
                symptomIds.add(rs.getString("id"));
            }
            updateSymptomEntry(symptomIds, entryId);
            stmt.close();
            conn.close();
            symptomsAdded = true;

        } catch (Exception e) {
            throw e;

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return symptomsAdded;
    }

    void updateSymptomEntry(List<String> pIds, String pEntryId) throws Exception {
        Statement stmt = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sql;
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            for (String i : pIds) {
                //System.out.println("Ingredient id: " + i);

                sql = "INSERT INTO symptomentry (symptomId, entryId)"
                        + "VALUES (" + i + "," + pEntryId + ")";
                stmt.executeUpdate(sql);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            throw e;

        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
    }

    List<Ingredient> gatherIngredients(String entryId) throws Exception {
        List<String> ingredId = new ArrayList();
        List<Ingredient> theIngredients;
        Statement stmt = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;

            sql = "SELECT ingredientId FROM ingredientId WHERE entryId=" + entryId;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                ingredId.add(rs.getString("ingredientId"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        theIngredients = gatherIngredientName(ingredId);
        return theIngredients;
    }

    List<Ingredient> gatherIngredientName(List<String> ingredId) throws Exception {
        List<Ingredient> theIngredients = new ArrayList();
        Ingredient temp;
        Statement stmt = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;

            for (String theId : ingredId) {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                sql = "SELECT Name FROM ingredients WHERE id=" + theId;
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                temp = new Ingredient(rs.getString("Name"), theId);
                theIngredients.add(temp);

                //rs.close();
                stmt.close();
                conn.close();

            }

        } catch (Exception e) {
            throw e;

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return theIngredients;
    }

    List<Symptom> gatherSymptoms(String entryId) throws Exception {
        List<Symptom> theSymptoms;
        List<String> symptomId = new ArrayList();
        Statement stmt = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;

            sql = "SELECT symptomId FROM symptomentry WHERE entryId=" + entryId;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                symptomId.add(rs.getString("symptomId"));
                //System.out.println("id: " + rs.getString("symptomId"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            throw e;

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        theSymptoms = gatherSymptomNames(symptomId);
        return theSymptoms;
    }

    List<Symptom> gatherSymptomNames(List<String> symptomId) throws Exception {
        Symptom temp = null;
        Statement stmt = null;
        Connection conn = null;
        List<Symptom> theSymptoms = new ArrayList();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;

            for (String theId : symptomId) {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                sql = "SELECT Name FROM symptoms WHERE id=" + theId;
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                temp = new Symptom(rs.getString("Name"), theId);
                theSymptoms.add(temp);
                rs.close();
                stmt.close();
                conn.close();

            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return theSymptoms;
    }

    Boolean addUser(String userName, String pass) throws Exception {
        Boolean userAdded = false;

        Symptom temp = null;
        Statement stmt = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            sql = "INSERT INTO users (userName, password) VALUES ('" + userName
                    + "', '" + pass + "')";
            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
            userAdded = true;

        } catch (Exception e) {

            throw e;

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return userAdded;
    }

    Boolean upDateEntry(List<String> pIngredients, List<String> pSymptoms,
            String pComments, int entryId, String userId) throws Exception {
        Boolean additionComplete = false;
        Boolean entryAdded = false;

        Statement stmt = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sql;
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            sql = "UPDATE entries SET comments ='" + pComments + "' WHERE id = " + entryId;

            stmt.executeUpdate(sql);

            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
            conn.close();
            entryAdded = true;

        } catch (Exception e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }

        }
        String sEntryId = entryId + "";
        if (addIngredients(pIngredients, sEntryId)
                && addSymptoms(pSymptoms, sEntryId) && entryAdded) {
            additionComplete = true;
        }

        return additionComplete;
    }
}
