import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        System.out.println("------client------");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入用户名：");
        String name = br.readLine();
        //建立连接：使用Socket创建客户端+服务器的地址和端口号
        Socket client = new Socket("175.24.53.216",2333);
        //客户端发送消息
        new Thread(new ClientSend(client,name)).start();
        //获取消息
        new Thread(new ClientReceive(client)).start();
    }
}