package org.programa;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Clase que se conecta a una base de datos para obtener los datos solicitados y crear un fichero plano de texto con los mismos
 *
 * @author Mario Vega Grund
 * @version 1.0.0
 */
public class MyProgram {
    private static final String user = "root";
    private static final String password = "";
    private static final String dataBaseName = "programa";
    private static final String hostName = "localhost";
    private static final String port = "3306";
    private static final String connectionString = "jdbc:mysql://";


    private static final String providersTable = "proveedores";
    private static final String clientIdField = "id_cliente";


    private static final String fileName = "outputPrograma";
    private static final String fileExtension = "txt";


    private static final ArrayList<String> linesToWrite = new ArrayList<>();
    private static String error = "";

    /**
     * Clase principal de ejecución del proyecto
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            String clientIdParameter = getClientParameter();
            if (clientIdParameter != null) {
                Connection con = createConnection();
                if (con != null) {
                    executeQuery(con, "SELECT * FROM `" + providersTable + "` WHERE " + clientIdField + " = " + clientIdParameter);

                    File outputFile = generateFile();
                    if (outputFile != null) {
                        writeIntoFile();
                    }

                    con.close();
                } else {
                    System.out.println("Connection to data base \"" + dataBaseName + "\" failed! - " + error);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Función que recibe por consola el parámetro deseado y comprueba si es o no correcto para usarse en el programa
     *
     * @return La ID si es correcta para ser utilizada o null si, en cambio, no lo es
     */
    private static String getClientParameter() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the client ID:\n");
        String clientIdParameter = sc.nextLine();
        clientIdParameter = clientIdParameter.trim();
        try {
            Integer.parseInt(clientIdParameter);
            return clientIdParameter;
        } catch (Exception e) {
            System.out.println("Entered ID is not valid, it must be a number.");
            return null;
        }
    }

    /**
     * Función que establece la conexión con la Base de Datos
     *
     * @return la conexión si se ha podido establecer o null si no se ha podido
     */
    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(connectionString + hostName + ":" + port + "/" + dataBaseName, user, password);
        } catch (SQLException e) {
            error = Arrays.toString(e.getStackTrace());
        }
        return null;
    }

    /**
     * Método que ejecuta la consulta recibida por parámetro contra la base de datos también recibida por parámetro e introduce el resultado en la variable "linesToWrite"
     *
     * @param con   conexión ya establecida con la base de datos
     * @param query consulta a ejecutar contra a la base de datos
     */
    private static void executeQuery(Connection con, String query) {
        try {
            boolean isEmpty = true;

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                if (isEmpty) {
                    linesToWrite.add("id_proveedor  -   nombre  -   fecha de alta   -   id_cliente");
                }
                isEmpty = false;
                linesToWrite.add(rs.getInt(1) + "           -   " + rs.getString(2) + "     -   " + rs.getDate(3) + "       -   " + rs.getInt(4));
            }

            if (isEmpty) {
                System.out.println("The client has no providers associated.");
            }
        } catch (SQLException e) {
            System.out.println("Error during query \"" + query + "\" execution!");
        }
    }

    /**
     * Función que crea u obtiene, si ya ha existía, un fichero
     *
     * @return fichero si se ha podido crear o null si no
     */
    private static File generateFile() {
        try {
            File outputFile = new File(fileName + "." + fileExtension);
            if (outputFile.createNewFile()) {
                System.out.println("File \"" + outputFile.getName() + "\" created.");
            }
            return outputFile;
        } catch (IOException e) {
            System.out.println("An error occurred during file creation.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método que escribe la información previamente obtenida desde la qeury en el fichero previamente creado
     */
    private static void writeIntoFile() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName + "." + fileExtension), StandardCharsets.UTF_8))) {
            for (String line : MyProgram.linesToWrite) {
                writer.write(line + "\r\n");
            }
        } catch (IOException ex) {
            System.out.println("Error while writing the file.");
        }
    }
}