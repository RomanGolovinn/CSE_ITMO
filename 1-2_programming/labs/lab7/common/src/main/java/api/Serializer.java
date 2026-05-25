package api;

import java.io.*;

public class Serializer {

    /**
     * Превращает любой объект (Request или Response) в массив байт для UDP
     */
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Превращает массив байт, пришедший по UDP, обратно в объект
     */
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        return objectInputStream.readObject();
    }
}