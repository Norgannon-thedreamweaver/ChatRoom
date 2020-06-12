import java.util.ArrayList;
import java.sql.*;
/**
 * 这个类用来存储用户
 * @author 李晓洲，邢湧喆，王博瑞
 * 
 */
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

    /**
     * 根据用户名搜索用户
     * @param name 用户名
     * @return User 用户，没有该用户则返回null
     * 
     */
    public User getUserByName(String name){
        name=name.toUpperCase();
        for (User user : this.User_list)
            if (name.equals(user.getName().toUpperCase()))
                return user;
        return null;
    }
    /**
     * 根据用户名搜索管理员
     * @param name 管理员
     * @return User 管理员，没有该管理员则返回null
     * 
     */
    public User getAdminByName(String name){
        name=name.toUpperCase();
        for (User user : this.Admin_list)
            if (name.equals(user.getName().toString().toUpperCase()))
                return user;
        return null;
    }
    /**
     * 根据用户名检查用户是否存在
     * @param name 用户名
     * @return boolean 该用户存在返回false，否则返回true
     * 
     */
    public boolean checkUserExist(String name){
        name=name.toUpperCase();
        for (User user : this.User_list)
            if (user.getName().toUpperCase().equals(name)) {
                return false;
            }
        return true;
    }
    /**
     * 添加新用户
     * @param name 用户名,password 密码
     * @return int -1表示用户名不合法，-2表示密码不合法，-3表示用户已存在，1表示添加成功
     * 
     */
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

    /**
     * 用户登录
     * @param name 用户名,passwd 密码
     * @return int -1表示用户名不存在，-2表示密码不正确，1表示登录成功
     * 
     */
    public int UserLogin(String name,String passwd){
        User user=getUserByName(name);
        if(user==null)
            return -1;
        if(!user.getPassword().equals(passwd))
            return -2;
        return 1;
    }
    /**
     * 用户注册
     * @param name 用户名,passwd 密码
     * @return int -1表示注册失败，1表示注册成功
     * 
     */
    public int UserSignup(String name,String passwd){
        if(UserList.getUserList().addUser(name, passwd)!=1)
            return -1;
        return 1;
    }
    /**
     * 管理员登录
     * @param name 用户名,passwd 密码
     * @return int -1表示管理员不存在，-2表示密码不正确，1表示登录成功
     * 
     */
    public int AdminLogin(String name,String passwd){
        User admin=getAdminByName(name);
        if(admin==null)
            return -1;
        if(!admin.getPassword().equals(passwd))
            return -2;
        return 1;
    }
}
