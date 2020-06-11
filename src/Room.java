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
    public void addMember(Socket client,DataInputStream input,DataOutputStream output,User user,String name){
        RoomThread roommember=new RoomThread(client,input,output,user,name,this.RID,this);
        this.MemberList.add(roommember);
        new Thread(roommember).start();
    }
    public void removeMember(String name){
        RoomThread m=getMemberByName(name);
        if(m!=null){
            this.MemberList.remove(m);

        }
    }
    public ArrayList<RoomThread> getMemberList(){
        return this.MemberList;
    }
    public String getMemberString(){
        StringBuilder ret= new StringBuilder("~user list:");
        for(RoomThread member:this.MemberList){
            ret.append(member.getName());
            ret.append(",");
        }
        return ret.toString();
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
        try {
            for(int i=5;i>0;i--){
                for(RoomThread member:this.MemberList){
                    member.send("群聊即将爆破："+i);

                }
                Thread.sleep(1000);
            }
            ArrayList<RoomThread> tmp=this.MemberList;
            for(RoomThread member:tmp){
                member.send("~close");
                member.setRunning(false);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void release() {
        Server.getAllRoom().remove(this);

    }
    @Override
    public void run() {
        while(this.isRunning&&this.MemberList.size()>0){
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