import java.util.Scanner;

public class Main {
    private static void usage() {
        System.out.println("Verfügbare Befehle:");
        System.out.println("  zeigen | show");
        System.out.println("  hinzufuegen | add <vorname> <nachname> <adresse> <alter>");
        System.out.println("  loeschen | delete <id>");
        System.out.println("  aktualisieren | aendern | update <id> <neuerVorname> <neuerNachname> <neueAdresse> <neuesAlter>");
        System.out.println("  exit | quit");
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            // Interaktiver Modus
            Funktionen db = new Funktionen();
            db.erstelleTabelle();
            Scanner scanner = new Scanner(System.in);
            usage();
            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine();
                if (line == null) break;
                line = line.trim();
                if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) break;
                if (line.isEmpty()) continue;
                processLine(line, db);
            }
            scanner.close();
            return;
        }
        Funktionen db = new Funktionen();
        db.erstelleTabelle();

        // Argumente aus der Kommandozeile zu einer einzigen Zeile zusammenfügen und verarbeiten
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(args[i]);
        }
        processLine(sb.toString(), db);
    }

    private static void processLine(String line, Funktionen db) {
        String lower = line.trim().toLowerCase();
        try {
            if (lower.startsWith("zeigen") || lower.startsWith("show")) {
                db.zeigen();
                return;
            }

            if (lower.startsWith("hinzufuegen") || lower.startsWith("add")) {
                String[] parts = line.split("\\s+", 5);
                if (parts.length < 5) {
                    System.out.println("hinzufuegen benötigt: <vorname> <nachname> <adresse> <alter>");
                    return;
                }
                String vorname = parts[1];
                String nachname = parts[2];
                String adresse = parts[3];
                int alter = Integer.parseInt(parts[4].trim());
                db.hinzufuegen(vorname, nachname, adresse, alter);
                return;
            }

            if (lower.startsWith("loeschen") || lower.startsWith("delete")) {
                String[] parts = line.split("\\s+", 2);
                if (parts.length < 2) {
                    System.out.println("loeschen benötigt: <id>");
                    return;
                }
                int idDel = Integer.parseInt(parts[1].trim());
                db.loeschen(idDel);
                return;
            }

            if (lower.startsWith("aktualisieren") || lower.startsWith("aendern") || lower.startsWith("update")) {
                String[] parts = line.split("\\s+", 6);
                if (parts.length < 6) {
                    System.out.println("aktualisieren benötigt: <id> <neuerVorname> <neuerNachname> <neueAdresse> <neuesAlter>");
                    return;
                }
                int idUp = Integer.parseInt(parts[1].trim());
                String neuerVorname = parts[2];
                String neuerNachname = parts[3];
                String neueAdresse = parts[4];
                int neuesAlter = Integer.parseInt(parts[5].trim());
                db.aktualisieren(idUp, neuerVorname, neuerNachname, neueAdresse, neuesAlter);
                return;
            }

            System.out.println("Unbekannter Befehl: " + line);
            usage();
        } catch (NumberFormatException e) {
            System.out.println("Zahlenformat-Fehler: " + e.getMessage());
        }
    }
}

