package model.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public abstract class Serializer {
    public static <T> T load(String path) {
        T obj = null;
        try (
                FileInputStream fileIn = new FileInputStream(path);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn)
        ) {
            obj = (T) objectIn.readObject();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("(!) File corrupt or not found: '" + path + "' creating new table");
            return null;
        }
    }

    public static <T> Status save(@NotNull T object, String path) {
        try (
                FileOutputStream fileOut = new FileOutputStream(path);
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
