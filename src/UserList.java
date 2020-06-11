import java.util.ArrayList;
import java.sql.*;

public class UserList {
    private static UserList Plist=new UserList();
    private UserList (){
        addUser("norgannon","a12345");
        addUser("user1","a12345");
        addUser("user2","a12345");
        addUser("user3","a12345");
        addUser("user4","a12345");
    }
    public static UserList getUserList() {
        return Plist;
    }
    private ArrayList<User> User_list=new ArrayList<User>();
    private ArrayList<User> Admin_list=new ArrayList<User>();

    public User getUserByName(String name){
        name=name.toUpperCase();
        for (User user : this.User_list)
            if (name.equals(user.getName().toUpperCase()))
                return user;
        return null;
    }
    public User getAdminByName(String name){
        name=name.toUpperCase();
        for (User user : this.Admin_list)
            if (name.equals(user.getName().toString().toUpperCase()))
                return user;
        return null;
    }

    public boolean checkUserExist(String name){
        name=name.toUpperCase();
        for (User user : this.User_list)
            if (user.getName().toUpperCase().equals(name)) {
                return false;
            }
        return true;
    }

    public int addUser(String name,String password){
        if(!User.checkName(name))
            return -1;
        if(!User.checkPasswd(password))
            return -2;
        if(!checkUserExist(name))
            return -3;
        User new_user= new User(name,password);
        User_list.add(new_user);
        return 1;
    }

    public int UserLogin(String name,String passwd){
        User user=getUserByName(name);
        if(user==null)
            return -1;
        if(!user.getPassword().equals(passwd))
            return -2;
        return 1;
    }
    public int UserSignup(String name,String passwd){
        if(UserList.getUserList().addUser(name, passwd)!=1)
            return -1;
        return 1;
    }
    
    public int AdminLogin(String name,String passwd){
        User admin=getAdminByName(name);
        if(admin==null)
            return -1;
        if(!admin.getPassword().equals(passwd))
            return -2;
        return 1;
    }
}
