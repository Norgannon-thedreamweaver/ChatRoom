import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSend implements Runnable{
    private static ClientSend self=null;
    private BufferedReader console;
    private DataOutputStream output;
    private Socket client;
    private boolean isRunning;
    private boolean isLogin;
    private boolean isRoom;

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

    public ClientSend(Socket client,String name) {
        isRunning = true;
        isLogin=false;
        isRoom=false;
        this.client = client;
        this.console = new BufferedReader(new InputStreamReader(System.in));
        try {
            output = new DataOutputStream(client.getOutputStream());
            //发送名称
            //send(name);
        } catch (IOException e) {
            System.out.println("Client error");
            this.release();
        }
        self=this;
    }
    public static ClientSend getClientSend(){
        return self;
    }
    @Override
    public synchronized void run() {
        while(!isLogin){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("send login");
        while(!isRoom){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("send room");
        while(isRunning) {
            String msg = this.getStringFromConsole();
            if(!msg.equals("")) {
                this.send(msg);
            }
        }
    }

    /**
     * 发送消息
     */
    public synchronized void send(String msg) {
        try {
            output.writeUTF(msg);
            output.flush();
        } catch (IOException e) {
            System.out.println("send error");
            release();
        }
    }

    /**
     * 从控制台获取消息
     */
    private String getStringFromConsole() {
        try {
            return console.readLine();
        } catch (IOException e) {
            System.out.println("console error");
            release();
        }
        return "";
    }

    //关闭
    private void release() {
        this.isRunning = false;
        try {
            this.output.close();
            this.console.close();
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}