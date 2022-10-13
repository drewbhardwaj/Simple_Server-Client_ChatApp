import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket cltSkt = new Socket("127.0.0.1", 5000);
            PrintWriter out = new PrintWriter(cltSkt.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(cltSkt.getInputStream()));
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
                            System.out.println("Server: " + msg);
                            msg = in.readLine();
                        }
                        System.out.println("Server out of Service;");
                        out.close();
                        cltSkt.close();
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
