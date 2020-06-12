import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 * 这个类是聊天室
 * @author 李晓洲，邢湧喆，王博瑞
 * 
 */
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
    /**
     * 为当前聊天室添加用户
     * @param client 用户socket,input client输入流,output client输出流,user 客户端用户,name 用户名
     * @return Nothing
     */
    public void addMember(Socket client,DataInputStream input,DataOutputStream output,User user,String name){
        RoomThread roommember=new RoomThread(client,input,output,user,name,this.RID,this);
        this.MemberList.add(roommember);
        new Thread(roommember).start();
    }
    /**
     * 为当前聊天室删除用户
     * @param name 用户名
     * @return Nothing
     */
    public void removeMember(String name){
        RoomThread m=getMemberByName(name);
        if(m!=null){
            this.MemberList.remove(m);

        }
    }
    /**
     * 获取聊天室用户列表
     * @return ArrayList<RoomThread> 用户列表
     * 
     */
    public ArrayList<RoomThread> getMemberList(){
        return this.MemberList;
    }
    /**
     * 获取String格式聊天室用户列表
     * @return String 用户列表
     * 
     */
    public String getMemberString(){
        StringBuilder ret= new StringBuilder("~user list:");
        for(RoomThread member:this.MemberList){
            ret.append(member.getName());
            ret.append(",");
        }
        return ret.toString();
    }
    /**
     * 根据用户名获取用户线程
     * @param name 用户名
     * @return RoomThread 用户线程
     * 
     */
    public RoomThread getMemberByName(String name){
        for(RoomThread member:this.MemberList){
            if(member.getName().equals(name))
                return member;
        }
        return null;
    }
    /**
     * 关闭当前房间
     * @return Nothing
     * 
     */
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
    /**
     * 结束当前线程
     * @return Nothing
     * 
     */
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