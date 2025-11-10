package migp.datos.persistencia;

import migp.datos.datosJuego.InterfazDao;

import java.io.*;
import java.nio.file.*;

public final class TempDatabase {

    //Ruta db temp -> C:/User/*AppData/Local/Temp/ + tempDbPath
    private static Path tempDbPath;

    //Evita crear instancias
    private TempDatabase() {}

    //Devuelve ruta de archivo db temporal en disco
    public static String getTempDBPath(){
        if (tempDbPath == null) {
            try {
                //Crear archivo rpg_temp_rpg.db
                tempDbPath = Files.createTempFile("rpg_temp_", ".db");
                //Elimina archivo al terminar ejecución
                tempDbPath.toFile().deleteOnExit();
                //Copiar contenido rpg.db del .jar a temp
                try (InputStream in = TempDatabase.class.getResourceAsStream("/rpg.db")) {
                    if(in!=null) {
                        Files.copy(in, tempDbPath, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("DB temporal creada en: " + tempDbPath);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("No se encontró rpg.db dentro del .jar");
                } catch (IOException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }catch (IOException e){
                System.out.println("ERROR: "+e.getMessage());
            }
        }
        return tempDbPath.toAbsolutePath().toString();
    }
}
