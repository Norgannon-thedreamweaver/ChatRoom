import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 这个类是聊天室线程
 * @author 李晓洲，邢湧喆，王博瑞
 * 
 */
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

    public void setRunning(boolean isRunning){
        this.isRunning=isRunning;
    }

    private User user;
    private String name;

    public RoomThread(Socket client,DataInputStream input,DataOutputStream output,User user,String name,int RID,Room room) {
        isRunning = true;
        this.client = client;
        this.input=input;
        this.output=output;
        this.user=user;
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

    /**
     * 从输入流接收消息
     * @return String 接收到的消息
     * 
     */
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
    /**
     * 向输出流发送消息
     * @param msg 要发送的消息
     * @return Nothing
     * 
     */
    public synchronized void send(String msg) {
        try {
            output.writeUTF(msg);
            output.flush();
        } catch (IOException e) {
            System.out.println("--------Chat send-------");
            release();
        }
    }
    
    /**
     * 发送私聊消息
     * @param name 对象名称,message 要发送的消息
     * @return Nothing
     * 
     */
    public void sendPrivate(String name,String message){
        RoomThread to=this.room.getMemberByName(name);
        if(to!=null){
            to.send(this.name+"悄悄对你说:"+message);
            this.send("你悄悄对"+name+"说:"+message);
        }

    }
    /**
     * 发送公共消息
     * @param message 要发送的消息,isSys 是否是系统消息
     * @return Nothing
     * 
     */
    public void sendPublic(String message,boolean isSys){
        for(RoomThread member:this.room.getMemberList()) {
            if(!isSys) {
                member.send(this.name+":"+message); //群聊消息
            }else {
                member.send(message); //系统消息
            }
        }
    }
    /**
     * 处理接收到的数据
     * @param cmd 接收到的数据
     * @return Nothing
     * 
     */
    public void Command(String cmd){
        String[] str=cmd.split(" ");
        if(str.length==1&&str[0].equals("-shutdown"))
            this.room.shutdown();
        else if(str.length==2&&cmd.startsWith("-get user:")){
            this.sendMessage(this.room.getMemberString(),true);
        }
        else if(str.length==2&&str[0].startsWith("-del")){
            RoomThread tmp=this.room.getMemberByName(str[1]);
            if(tmp!=null){
                tmp.send("~self leave");
            }
        }
        else if(str.length==1&&str[0].startsWith("-leave")){
            this.room.getMemberList().remove(this);
            this.send("~close");
            this.isRunning=false;
            this.sendMessage(this.room.getMemberString(),true);
        }
        else if(str.length==1&&str[0].startsWith("-forceleave")){
            this.room.getMemberList().remove(this);
            this.send("~close");
            this.isRunning=false;
            this.sendMessage(name+"被请出了房间",true);
            this.sendMessage(this.room.getMemberString(),true);
        }
        else
            this.send("wrong cmd");
    }
    /**
     * 发送消息
     * @param msg 要发送的消息,isSys 是否是系统消息
     * @return Nothing
     * 
     */
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
    /**
     * 退出聊天室关闭聊天室线程
     * @return Nothing
     * 
     */
    public void close(){
        ServerThread new_server=new ServerThread(client,input,output,user,name);
        Server.getServerThread().add(new_server);
        new Thread(new_server).start();
    }
    /**
     * 关闭线程
     * @return Nothing
     * 
     */
    public synchronized void release() {
        this.isRunning = false;
        try {
            this.input.close();
            this.output.close();
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //溜
        room.getMemberList().remove(this);

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
        close();
    }

}