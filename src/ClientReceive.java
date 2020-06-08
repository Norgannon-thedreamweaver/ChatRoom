import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReceive implements Runnable{
    private DataInputStream input;
    private Socket client;
    private boolean isRunning;

    public ClientReceive(Socket client) {
        this.isRunning = true;
        this.client = client;
        try {
            input = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("Client error");
            this.release();
        }
    }

    //接收消息
    public String receive() {
        String msg = "";
        try {
            msg = input.readUTF();
        } catch (IOException e) {
            System.out.println("Receive error");
            release();
        }
        return msg;
    }

    @Override
    public void run() {
        while(isRunning) {
            String msg = this.receive();
            if(!msg.equals("")) {
                System.out.println(msg);
            }
        }
    }

    //关闭
    private void release() {
        this.isRunning = false;
        try {
            this.input.close();
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}