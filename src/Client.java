import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 这个类启动客户端
 * @author 李晓洲，邢湧喆，王博瑞
 * 
 */
public class Client {
    public static void main(String[] args){
        System.out.println("------client------");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        while(!ClientUI.getClientUI().isConnect()){
            try{
                Socket client = new Socket("175.24.53.216",2333);
                //Socket client = new Socket("localhost",2333);

                ClientReceive receive=new ClientReceive(client);
                //客户端发送消息
                ClientSend send=new ClientSend(client,"name");

                new Thread(receive).start();
                new Thread(send).start();

                ClientUI.getClientUI().CE2Login();
                System.out.println("服务器连接成功");
            }
            catch (IOException e){
                ClientUI.getClientUI().init2CE();
                System.out.println("服务器连接失败");
            }
            finally {
                ClientUI.getClientUI().getFrame().setVisible(true);
            }
        }
    }
    /**
     * 这个方法将登录表单发送到客户端，并根据返回结果判断登录状态
     * @param username 用户输入的账号,password 用户输入的密码,send 发消息线程,receive 收消息线程
     * @return boolean 登录是否成功,成功为true
     */
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
    /**
     * 这个方法将注册表单发送到客户端，并根据返回结果判断注册状态
     * @param username 用户输入的账号,password 用户输入的密码,send 发消息线程,receive 收消息线程
     * @return boolean 注册是否成功,成功为true
     */
    public static boolean signup(String username,String password,ClientSend send,ClientReceive receive){
        //System.out.println("here");
    	String param="signup "+username+" "+password;
        send.send(param);
        String str=receive.receive();
        System.out.println(str);
        if(str.equals("signup-success")){
            send.setSignup(true);
            receive.setSignup(true);
            return true;
        }
        return false;
    }
    /**
     * 这个方法将创建聊天室表单发送到客户端，并返回房间号
     * @param secret 表示是否为私密聊天室,私密为true,password 房间密码,send 发消息线程,receive 收消息线程
     * @return int 创建成功返回聊天室id，创建失败返回0
     */
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
            send.setChat(true);
            receive.setChat(true);
            send.setRoom(true);
            receive.setRoom(true);
            return Integer.parseInt(str.substring(idx+1));
        }
        return 0;
    }
    /**
     * 这个方法将加入公共聊天室表单发送到客户端，并返回加入结果
     * @param RID 聊天室id,send 发消息线程,receive 收消息线程
     * @return int 加入聊天室成功返回1，否则返回0
     */
    public static int joinRoom(String RID,ClientSend send,ClientReceive receive){
        String param="join -public "+RID;
        send.send(param);
        String str=receive.receive();
        System.out.println(str);
        if (str.equals("join-success")){
            send.setChat(true);
            receive.setChat(true);
            send.setRoom(true);
            receive.setRoom(true);
            return 1;
        }
        return 0;
    }
    /**
     * 这个方法将加入私密聊天室表单发送到客户端，并返回加入结果
     * @param RID 聊天室id,password 房间密码,send 发消息线程,receive 收消息线程
     * @return int 加入聊天室成功返回1，密码错误返回-1，其他情况返回0
     */
    public static int joinRoom(String RID,String password,ClientSend send,ClientReceive receive){
        String param="join -private "+password+" "+RID;
        send.send(param);
        String str=receive.receive();
        System.out.println(str);
        if (str.equals("join-success")){
            send.setChat(true);
            receive.setChat(true);
            send.setRoom(true);
            receive.setRoom(true);
            return 1;
        }
        else if(str.equals("wrong-password"))
            return -1;
        return 0;
    }
    /**
     * 这个方法将用于获取房间内全部用户
     * @param selfname 本用户用户名,send 发消息线程,receive 收消息线程
     * @return String[][] 全部用户用户名
     */
    public static String[][] getUserList(String selfname,ClientSend send,ClientReceive receive){
        String param="-get user:"+selfname;
        send.send(param);
        String str=receive.receive();
        System.out.println(str);
        String[][] users=null;
        if(str.startsWith("~user list:")){
            int idx = str.indexOf(":");
            str=str.substring(idx+1);
            String[] user=str.split(",");
            users=new String[user.length][1];
            for(int i=0;i<user.length;i++){
                users[i][0]=user[i];
            }
        }
        return users;
    }
    /**
     * 这个方法将用于从房间内踢出用户
     * @param name 被踢出用户用户名,send 发消息线程
     * @return Nothing
     */
    public static void deleteUser(String name,ClientSend send){
        send.send("-del "+name);
    }
}