package com.bui.projects.util;

import lombok.experimental.UtilityClass;

import java.io.*;

@UtilityClass
public class ResourceFileLoader {
    public static File loadResourceAsTempFile(String resourceName) throws IOException {
        InputStream inputStream = ResourceFileLoader.class.getClassLoader().getResourceAsStream(resourceName);

        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + resourceName);
        }

        File tempFile = File.createTempFile("temp_", "_" + resourceName);
        tempFile.deleteOnExit(); // Удалится после завершения работы JVM

        try (OutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }
}
