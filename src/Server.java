import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 这个类是服务端
 * @author 李晓洲，邢湧喆，王博瑞
 * 
 */
public class Server {
    private int port;
    private static ArrayList<ServerThread> allThread = new ArrayList<ServerThread>();
    private static ArrayList<Room> allRoom = new ArrayList<Room>();
    private static Server self=new Server(2333);
    private ServerSocket socket;

    private Server(int port){
        this.port=port;
    }
    public static Server getServer(){
        return self;
    }
    public static ArrayList<ServerThread> getServerThread(){
        return allThread;
    }
    public static ArrayList<Room> getAllRoom(){
        return allRoom;
    }
    /**
     * 检查房间是否存在
     * @param RID 房间号
     * @return boolean 存在为true，否则为false
     */
    public static boolean checkRoomExist(int RID){
        for(Room room:allRoom){
            if(room.RID==RID)
                return true;
        }
        return false;
    }
    /**
     * 根据房间号获得房间
     * @param RID 房间号
     * @return Room 返回对应的房间号，不存在返回null
     */
    public static Room getRoomByRID(int RID){
        for(Room room:allRoom){
            if(room.RID==RID)
                return room;
        }
        return null;
    }

    public void run() throws IOException {
        System.out.println("------server------");
        //1.指定端口：使用ServerSocket创建服务器
        this.socket = new ServerSocket(this.port);
        //2.阻塞式等待连接accept
        while(true) {
            Socket client = this.socket.accept();
            System.out.println("一个客户端建立了连接");
            ServerThread c = new ServerThread(client);
            allThread.add(c);
            new Thread(c).start();
        }
    }
}