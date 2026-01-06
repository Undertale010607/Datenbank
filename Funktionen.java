import java.sql.*;

public class Funktionen {

    // Verbindung zur Datenbank herstellen
    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:sqlite.db";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC-Treiber nicht gefunden", e);
        }
        Connection conn = DriverManager.getConnection(url);
        if (conn == null) {
            throw new SQLException("Verbindung konnte nicht hergestellt werden");
        }
        return conn;
    }

    // 1. Tabelle erstellen (menschen)
    public void erstelleTabelle() {
        String sql = "CREATE TABLE IF NOT EXISTS menschen ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " vorname TEXT NOT NULL,"
                + " nachname TEXT NOT NULL,"
                + " adresse TEXT"
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            // Prüfen, ob die Spalte 'alter' existiert; falls nicht, hinzufügen
            boolean hasAlter = false;
            try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(menschen)")) {
                while (rs.next()) {
                    String colName = rs.getString("name");
                    if ("alter".equalsIgnoreCase(colName)) {
                        hasAlter = true;
                        break;
                    }
                }
            }
            if (!hasAlter) {
                stmt.execute("ALTER TABLE menschen ADD COLUMN \"alter\" INTEGER");
            }

            System.out.println("Tabelle wurde erstellt/aktualisiert.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 2. Hinzufügen (Create)
    public void hinzufuegen(String vorname, String nachname, String adresse, int alter) {
        String sql = "INSERT INTO menschen(vorname, nachname, adresse, \"alter\") VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vorname);
            pstmt.setString(2, nachname);
            pstmt.setString(3, adresse);
            pstmt.setInt(4, alter);
            pstmt.executeUpdate();
            System.out.println(vorname + " " + nachname + " wurde hinzugefügt.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 3. Ändern (Update)
    public void aktualisieren(int id, String neuerVorname, String neuerNachname, String neueAdresse, int neuesAlter) {
        String sql = "UPDATE menschen SET vorname = ?, nachname = ?, adresse = ?, \"alter\" = ? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, neuerVorname);
            pstmt.setString(2, neuerNachname);
            pstmt.setString(3, neueAdresse);
            pstmt.setInt(4, neuesAlter);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            System.out.println("Eintrag " + id + " wurde aktualisiert.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 4. Löschen (Delete)
    public void loeschen(int id) {
        String sql = "DELETE FROM menschen WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Eintrag " + id + " wurde gelöscht.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 5. Zeigen (Read) - alle Einträge anzeigen
    public void zeigen() {
        String sql = "SELECT id, vorname, nachname, adresse, \"alter\" FROM menschen";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("ID | Vorname | Nachname | Adresse | Alter");
            System.out.println("------------------------------------------------");
            while (rs.next()) {
                int id = rs.getInt("id");
                String vorname = rs.getString("vorname");
                String nachname = rs.getString("nachname");
                String adresse = rs.getString("adresse");
                int alter = rs.getInt("alter");
                String alterStr = rs.wasNull() ? "NULL" : Integer.toString(alter);
                System.out.println(id + " | " + vorname + " | " + nachname + " | " + adresse + " | " + alterStr);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
