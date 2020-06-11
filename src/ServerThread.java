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
    public ServerThread(Socket client,DataInputStream input,DataOutputStream output,User user,String  name) {
        isRunning = true;
        this.client=client;
        this.input=input;
        this.output=output;
        this.user=user;
        this.name=name;
        System.out.println(this.name+"回来了");
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

    //关闭
    public void release() {
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
        room.addMember(this.client,this.input,this.output,this.user,this.name);
        new Thread(room).start();
        Server.getServerThread().remove(this);
        this.isRunning=false;
        this.send("create-"+room.RID);

    }
    public void joinRoom(int RID){
        Room room=Server.getRoomByRID(RID);
        if(room!=null&&!room.secret){
            room.addMember(this.client,this.input,this.output,this.user,this.name);
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
                room.addMember(this.client,this.input,this.output,this.user,this.name);
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
    public void Signup(String username,String password){
        if(UserList.getUserList().UserSignup(username,password)==1){
        	//System.out.println("here");
            this.send("signup-success");
        }
        else{
            this.send("signup-fail");
        }
    }
    public void Command(String msg){
        String[] str=msg.split(" ");
        System.out.println("cmd:"+msg);
        if(user==null){
            if(str.length==3&&str[0].equals("login")){
                Login(str[1],str[2]);
            }
            else if(str.length==3&&str[0].equals("signup")){
            	//System.out.println("here");
                Signup(str[1],str[2]);
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
                System.out.println("server Thread receive:"+msg);
                Command(msg);
            }
        }
    }
}