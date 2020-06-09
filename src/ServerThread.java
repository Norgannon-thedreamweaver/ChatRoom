import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

public class ServerThread implements Runnable{
    private DataInputStream input;
    private DataOutputStream output;
    private Socket client;
    private boolean isRunning;
    private User user=null;
    private String name;

    public ServerThread(Socket client) {
        isRunning = true;
        this.client = client;
        try {
            input = new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());
            //获取名称
            System.out.println(this.name+"来了");
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
    public void send(String msg) {
        try {
            output.writeUTF(msg);
            output.flush();
        } catch (IOException e) {
            System.out.println("--------Chat send-------");
            release();
        }
    }

    public void sendOthers(String msg,boolean isSys) {
        boolean isPrivate = msg.startsWith("@");
        ArrayList<ServerThread> all=Server.getServerThread();
        if(isPrivate) { //私聊
            int idx = msg.indexOf(":");
            //获取目标和数据
            String targetName = msg.substring(1,idx);
            msg = msg.substring(idx+1);
            for(ServerThread other:all) {
                if(other.name.equals(targetName)) { //目标
                    other.send(this.name+"悄悄的对你说："+msg);
                    break;
                }
            }
        }else { //群聊
            for(ServerThread other:all) {
                if(this==other) { //自己
                    continue;
                }
                if(!isSys) {
                    other.send(this.name+"对所有人说："+msg); //群聊消息
                }else {
                    other.send(msg); //系统消息
                }
            }
        }
    }
    //关闭
    public void release() {
        //sendOthers(this.name+"下线了！",true);
        this.isRunning = false;
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
    public void createRoom(boolean secret,String password){
        Room room=new Room(secret,password);
        Server.getAllRoom().add(room);
        room.addMember(this.client,this.input,this.output,this.name);
        new Thread(room).start();
        Server.getServerThread().remove(this);
        this.isRunning=false;
        this.send("create-"+room.RID);
    }
    public void joinRoom(int RID){
        Room room=Server.getRoomByRID(RID);
        if(room!=null&&!room.secret){
            room.addMember(this.client,this.input,this.output,this.name);
            Server.getServerThread().remove(this);
            this.isRunning=false;
            this.send("join-success");
        }
        else{
            this.send("no-room-found");
        }
    }
    public void joinRoom(int RID,String password){
        Room room=Server.getRoomByRID(RID);
        if(room!=null&&room.secret){
            if(room.getPassword().equals(password)){
                room.addMember(this.client,this.input,this.output,this.name);
                Server.getServerThread().remove(this);
                this.isRunning=false;
                this.send("join-success");
            }
            else
                this.send("wrong-password");
        }
        else{
            this.send("no-room-found");
        }
    }
    public void Login(String username,String password){
        if(UserList.getUserList().UserLogin(username,password)==1){
            this.send("login-success");
            this.name=username;
            this.user=UserList.getUserList().getUserByName(name);
        }
        else{
            this.send("login-fail");
        }
    }
    public void Command(String msg){
        String[] str=msg.split(" ");
        if(user==null){
            if(str.length==3&&str[0].equals("login")){
                Login(str[1],str[2]);
            }
        }
        else{
            if(str.length==3&&str[0].equals("join")&&str[1].equals("-public")&&Pattern.matches("^[0-9]+$",str[2])){
                joinRoom(Integer.parseInt(str[2]));
            }
            if(str.length==4&&str[0].equals("join")&&str[1].equals("-private")&&Pattern.matches("^[0-9]+$",str[3])){
                joinRoom(Integer.parseInt(str[3]),str[2]);
            }
            if(str.length==2&&str[0].equals("create")&&str[1].equals("-public")){
                createRoom(false,null);
            }
            if(str.length==3&&str[0].equals("create")&&str[1].equals("-private")){
                createRoom(true,str[2]);
            }
        }


    }
    @Override
    public void run() {
        while(isRunning) {
            String msg = receive();
            if(!msg.equals("")) {
                Command(msg);
            }
        }
    }
}