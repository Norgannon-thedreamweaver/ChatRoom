import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Room implements Runnable{
    boolean isRunning;
    int RID;
    boolean secret;
    private String password;
    ArrayList<RoomThread> MemberList=new ArrayList<>();

    public String getPassword(){
        return password;
    }
    public Room(boolean secret,String password){
        this.isRunning=true;
        this.secret=secret;
        this.password=password;
        do {
            this.RID = new Random().nextInt(9000) + 1000;
        } while (Server.checkRoomExist(this.RID));
    }
    public void addMember(Socket client,DataInputStream input,DataOutputStream output,String name){
        RoomThread roommember=new RoomThread(client,input,output,name,this.RID,this);
        this.MemberList.add(roommember);
        new Thread(roommember).start();
    }
    public ArrayList<RoomThread> getMemberList(){
        return this.MemberList;
    }
    public RoomThread getMemberByName(String name){
        for(RoomThread member:this.MemberList){
            if(member.getName().equals(name))
                return member;
        }
        return null;
    }
    public void shutdown(){
        this.isRunning=false;
        System.out.println("ready to shutdown");
    }
    public void release() {
        try {
            for(int i=10;i>0;i--){
                for(RoomThread member:this.MemberList){
                    member.sendMessage("群聊即将爆破："+i,true);
                    Thread.sleep(1000);
                }
            }
            for(RoomThread member:this.MemberList){
                member.release();
            }
            Server.getAllRoom().remove(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void run() {
        while(this.isRunning){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("not alive");
        this.release();
    }
}