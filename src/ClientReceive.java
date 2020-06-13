import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
/**
 * 这个类是收消息线程类
 * @author 李晓洲，邢湧喆，王博瑞
 * 
 */
public class ClientReceive implements Runnable{
    private static ClientReceive self=null;
    private DataInputStream input;
    private Socket client;
    private boolean isRunning;
    private boolean isLogin;
    private boolean isSignup;
    private boolean isRoom;
    private boolean isChat;


    public boolean isRoom() {
        return isRoom;
    }
    public void setRoom(boolean room) {
        isRoom = room;
    }
    public boolean isLogin() {
        return isLogin;
    }
    public void setLogin(boolean login) {
        isLogin = login;
    }
    public boolean isSignup() {
        return isSignup;
    }
    public void setSignup(boolean signup) {
    	isSignup = signup;
    }
    public boolean isChat() {
        return isChat;
    }
    public void setChat(boolean chat) {
        isChat = chat;
    }
    public ClientReceive(Socket client) {
        this.isRunning = true;
        isLogin=false;
        isRoom=false;
        isChat=false;
        this.client = client;
        try {
            input = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("Client error");
            this.release();
        }
        self=this;
    }
    public static ClientReceive getClientReceive(){
        return self;
    }
    /**
     * 这个方法从服务端接收消息
     * @return String 接收到的消息
     */
    public synchronized String receive() {
        String msg = "";
        try {
            msg = input.readUTF();
        } catch (IOException e) {
            System.out.println("Receive error");
            release();
        }
        return msg;
    }

    /**
     * 重写run方法
     * @return Nothing
     */
    @Override
    public synchronized void run() {
        while(isRunning){
            System.out.println("receive start");
            while(!isLogin&&isRunning){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(!isRoom&&isRunning){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(isChat&&isRunning) {
                String msg = this.receive();
                if(!msg.equals("")) {
                    System.out.println(msg);
                    Command(msg);
                }
            }
        }

    }
    /**
     * 这个方法从客户端接收数据并处理
     * @param msg 接收到的数据
     * @return Nothing
     */
    public void Command(String msg){
        if(msg.startsWith("~user list:")){
            int idx = msg.indexOf(":");
            msg=msg.substring(idx+1);
            String[] user=msg.split(",");
            String[][] users=new String[user.length][1];
            for(int i=0;i<user.length;i++){
                users[i][0]=user[i];
            }
            ClientUI.getClientUI().getUser().updataList(users);
        }
        else if(msg.startsWith("~close")){
            ClientUI.getClientUI().Chat2Room();
        }
        else if(msg.equals("~self leave")){
            ClientSend.getClientSend().send("-forceleave");
        }
        else{
            ClientUI.getClientUI().getChat().addstring(msg+"\n\n");
        }
    }
    /**
     * 程序退出时结束线程，异常出现时自动执行
     * @return Nothing
     */
    private void release() {
        this.isRunning = false;
        try {
            this.input.close();
            this.client.close();
            int result = JOptionPane.showConfirmDialog(
                    ClientUI.getClientUI().getFrame(),
                    "网络错误,即将退出",
                    "提示",
                    JOptionPane.WARNING_MESSAGE
            );
            System.out.println("选择结果: " + result);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}