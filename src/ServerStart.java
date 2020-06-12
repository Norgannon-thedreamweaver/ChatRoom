import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/**
 * 这个类开始运行服务端
 * @author 李晓洲，邢湧喆，王博瑞
 * 
 */
public class ServerStart {


    public static void main(String[] args) throws IOException {
        try{
            InetAddress address=InetAddress.getLocalHost();
            System.out.println("计算机名："+address.getHostName());
            System.out.println("IP地址："+address.getHostAddress());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Server.getServer().run();
    }
}
