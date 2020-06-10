import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReceive implements Runnable{
    private static ClientReceive self=null;
    private DataInputStream input;
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

    public ClientReceive(Socket client) {
        this.isRunning = true;
        isLogin=false;
        isRoom=false;
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
    //接收消息
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

    @Override
    public synchronized void run() {
        while(!isLogin){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(!isRoom){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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