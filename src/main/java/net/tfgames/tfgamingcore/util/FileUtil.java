package net.tfgames.tfgamingcore.util;

import java.io.*;
import java.nio.file.Files;

public class FileUtil {

    public static void copy(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            String[] files = source.list();
            if (files == null) return;
            for (String file : files) {
                File newSource = new File(source, file);
                File newDestination = new File(destination, file);
                copy(newSource, newDestination);
            }
        } else {
            InputStream in = Files.newInputStream(source.toPath());
            OutputStream out = Files.newOutputStream(destination.toPath());

            byte[] buffer = new byte[1024];

            int length;
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return;
            for (File child : files) {
                delete(child);
            }
        }

        file.delete();
    }

}
