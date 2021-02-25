package ru.geekbrains.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public ClientChat() {
        try {
            socket = new Socket("127.0.0.1", 8989);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    sendMessage(scanner.nextLine());
                }
            })
                    .start();


            while (true) {
                System.out.println(in.readUTF());
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    private void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
