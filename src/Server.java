import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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

    public static boolean checkRoomExist(int RID){
        for(Room room:allRoom){
            if(room.RID==RID)
                return true;
        }
        return false;
    }
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