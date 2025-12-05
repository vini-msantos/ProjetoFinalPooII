package model.util;

import java.io.*;

public abstract class Serializer {
    private final static String directoryPath = "data";
    public static <T> T load(String path) {
        T obj = null;
        try (
                FileInputStream fileIn = new FileInputStream(directoryPath + "/" + path);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn)
        ) {
            obj = (T) objectIn.readObject();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("(!) File corrupt or not found: '" + path + "' creating new table");
            return null;
        }
    }

    public static <T> Status save(T object, String path) {
        File directory = new File("data");
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdir();
        }

        try (
                FileOutputStream fileOut = new FileOutputStream(directoryPath + "/" + path);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)
        ) {
            objectOut.writeObject(object);
            return Status.OK;
        } catch (IOException e) {
            System.out.println(e);
            return Status.ERROR;
        }
    }
}
