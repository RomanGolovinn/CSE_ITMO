package io.net;

import common.Request;
import common.Response;
import common.Serializer;
import models.Flat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {
    Serializer serializer = new Serializer();
    DatagramSocket socket;
    InetAddress address;
    int port;

    public Client(InetAddress address, int port) throws SocketException {
        this.socket = new DatagramSocket();
        this.address = address;
        this.port = port;
    }

    public Response sendCommand(String commandName, String arg, Flat flat) throws IOException {
        Request req = new Request(commandName, arg, flat);
        byte[] serializedReq = new byte[0];
        try {
            serializedReq = this.serializer.serialize(req);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        socket.send(new DatagramPacket(
                serializedReq, serializedReq.length, this.address, this.port
        ));

        socket.setSoTimeout(3500);

        byte[] receiveBuffer = new byte[65535];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

        socket.receive(receivePacket);

        byte[] responseBytes = new byte[receivePacket.getLength()];
        System.arraycopy(receivePacket.getData(), 0, responseBytes, 0, receivePacket.getLength());

        try {
            return (Response) this.serializer.deserialize(responseBytes);
        } catch (ClassNotFoundException e) {
            throw new IOException("Неизвестный класс ответа от сервера", e);
        }
    }
}
