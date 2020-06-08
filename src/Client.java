import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        System.out.println("------client------");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Socket client = new Socket("175.24.53.216",2333);
        //Socket client = new Socket("localhost",2333);
        //客户端发送消息
        new Thread(new ClientSend(client,"name")).start();
        //获取消息
        new Thread(new ClientReceive(client)).start();
    }
}