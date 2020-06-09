import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        boolean isConnect=false;
        System.out.println("------client------");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        JFrame frame = new JFrame("Login");
        frame.setSize(250, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel=new LoginUI().getJp(),CEPanel=new ConnectError().getCE();
        while(!isConnect){
            try{
                //Socket client = new Socket("175.24.53.216",2333);
                Socket client = new Socket("localhost",2333);

                ClientReceive receive=new ClientReceive(client);
                //客户端发送消息
                ClientSend send=new ClientSend(client,"name");

                new Thread(receive).start();
                new Thread(send).start();

                frame.remove(CEPanel);
                frame.add(loginPanel);
                isConnect=true;
                System.out.println("服务器连接成功");
            }
            catch (ConnectException e){
                System.out.println("服务器连接失败");
                frame.add(CEPanel);
            }
            finally {
                frame.setVisible(true);
            }
        }

    }
    public static boolean login(String username,String password,ClientSend send,ClientReceive receive){
        String param="login "+username+" "+password;
        send.send(param);
        String str=receive.receive();
        System.out.println(str);
        if(str.equals("login-success")){
            send.setLogin(true);
            receive.setLogin(true);
            return true;
        }
        return false;
    }
    public static int createRoom(boolean secret,String password,ClientSend send,ClientReceive receive){
        String param="create ";
        if(secret)
            param+="-private ";
        else
            param+="-public ";
        if(secret)
            param+=password;
        send.send(param);
        String str=receive.receive();
        System.out.println(str);
        if(str.startsWith("create-")){
            int idx=str.indexOf("-");
            send.setRoom(true);
            receive.setRoom(true);
            return Integer.parseInt(str.substring(idx+1));
        }
        return 0;
    }
    public static int joinRoom(String RID,ClientSend send,ClientReceive receive){
        String param="join -private "+RID;
        send.send(param);
        String str=receive.receive();
        System.out.println(str);
        if (str.equals("join-success")){
            send.setRoom(true);
            receive.setRoom(true);
            return 1;
        }
        return 0;
    }
    public static int joinRoom(String RID,String password,ClientSend send,ClientReceive receive){
        String param="join -private "+password+" "+RID;
        send.send(param);
        String str=receive.receive();
        System.out.println(str);
        if (str.equals("join-success")){
            send.setRoom(true);
            receive.setRoom(true);
            return 1;
        }
        else if(str.equals("wrong-password"))
            return -1;
        return 0;
    }
}