package ru.geekbrains.chat.server;

import ru.geekbrains.chat.server.auth.AuthenticationService;
import ru.geekbrains.chat.server.auth.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * 1. Multiclient
 * 2. Login to chat (auth)
 * 3. Execute constraints (Unique name, already existing client, etc.)
 * 4. IN\OUT messages + broadcast
 */
public class Server {
    private final ServerSocket serverSocket;
    private final AuthenticationService authenticationService;
    private final Set<ClientHandler> handlers;

    public Server() {
        authenticationService = new AuthenticationService();
        handlers = new HashSet<>();
        try {
            serverSocket = new ServerSocket(8989);
            init();
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    private void init() throws IOException {
        while (true) {
            System.out.println("Server is waiting for a connection...");
            Socket client = serverSocket.accept();
            System.out.println("Client accepted: " + client);
            new ClientHandler(this, client);
        }
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public boolean isNicknameFree(String nickname) {
        for (ClientHandler handler : handlers) {
            if (handler.getName().equals(nickname)) {
                return false;
            }
        }
        return true;
    }

    public void broadcast(String message) {
        for (ClientHandler handler : handlers) {
            handler.sendMessage(message);
        }
    }

    public void subscribe(ClientHandler handler) {
        handlers.add(handler);
    }

    public void unsubscribe(ClientHandler handler) {
        handlers.remove(handler);
    }
}
