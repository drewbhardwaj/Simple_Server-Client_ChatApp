import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serS = new ServerSocket(5000);
            Socket skt = serS.accept();
            PrintWriter out = new PrintWriter(skt.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            Scanner sc = new Scanner(System.in);

            // threads to send and receive
            Thread sender = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while (true) {
                        msg = sc.nextLine();
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            sender.start();
            Thread receive = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg != null) {
                            System.out.println("Client: " + msg);
                            msg = in.readLine();
                        }
                        System.out.println("Client Disconnected;");
                        out.close();
                        skt.close();
                        serS.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            receive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
