import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class RoomThread implements Runnable{
    private final int RID;
    private final Room room;
    private DataInputStream input;
    private DataOutputStream output;
    private Socket client;
    private boolean isRunning;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private User user;
    private String name;

    public RoomThread(Socket client,DataInputStream input,DataOutputStream output,String name,int RID,Room room) {
        isRunning = true;
        this.client = client;
        this.input=input;
        this.output=output;
        this.name=name;
        this.RID=RID;
        this.room=room;
        try {
            System.out.println(this.name+"来了");
            this.sendMessage(this.name+"来了",true);
        }catch(Exception e) {
            release();
        }
    }

    //接收消息
    public String receive() {
        String msg = "";
        try {
            msg = input.readUTF();
        } catch (IOException e) {
            System.out.println("-------Chat receive------");
            release();
        }
        return msg;
    }
    //发送消息
    public synchronized void send(String msg) {
        try {
            output.writeUTF(msg);
            output.flush();
        } catch (IOException e) {
            System.out.println("--------Chat send-------");
            release();
        }
    }

    public void sendPrivate(String name,String message){
        RoomThread to=this.room.getMemberByName(name);
        if(to!=null)
            to.send(this.name+"悄悄对你说:"+message);
    }
    public void sendPublic(String message,boolean isSys){
        for(RoomThread member:this.room.getMemberList()) {
            if(!isSys) {
                member.send(this.name+"："+message); //群聊消息
            }else {
                member.send(message); //系统消息
            }
        }
    }
    public void Command(String cmd){
        if(cmd.equals("-shutdown"))
            this.room.shutdown();
        else
            this.send("wrong cmd");
    }
    public void sendMessage(String msg,boolean isSys) {
        boolean isPrivate = msg.startsWith("@");
        boolean isCmd=msg.startsWith("-");
        if(isCmd){
            Command(msg);
            return;
        }
        ArrayList<RoomThread> all=this.room.getMemberList();
        if(isPrivate) { //私聊
            int idx = msg.indexOf(":");
            String to = msg.substring(1,idx);
            msg = msg.substring(idx+1);
            sendPrivate(to,msg);
        }
        else { //群聊
            sendPublic(msg,isSys);
        }
    }
    //关闭
    public synchronized void release() {
        this.isRunning = false;
        //sendMessage(this.name+"溜了！",true);
        try {
            this.input.close();
            this.output.close();
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //溜
        Server.getServerThread().remove(this);

    }

    @Override
    public void run() {
        while(isRunning) {
            String msg = receive();
            if(!msg.equals("")) {
                System.out.println("server receive from "+this.getName()+":"+msg);
                sendMessage(msg,false);
            }
        }
    }
}