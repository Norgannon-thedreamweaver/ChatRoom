import java.util.regex.Pattern;

public class User {
    private boolean admin=false;
    private String name;
    private String gender="未知";
    private String Password;

    public User(String name,String password){
        setName(name);
        setPassword(password);
    }

    public String getName(){return this.name;}
    public String getGender(){
        return this.gender;
    }
    public boolean getAdmin(){
        return this.admin;
    }

    public String[] getAll(){
        String[] ret=new String[3];
        ret[0]=this.name;
        ret[1]=this.gender;
        ret[2]=this.admin?"true":"false";
        return ret;
    }
    static public boolean checkName(String name){
        if(Pattern.matches("^[A-Za-z1-9]+$",name))
            return true;
        else
            return false;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setGender(String gender){
        this.gender=gender;
    }

    static public boolean checkPasswd(String passwd){
        int upper=0,lower=0,number=0,other=0;
        if(passwd.length()>18||passwd.length()<6)
            return false;
        for(int i=0;i<passwd.length();i++){
            if((int)passwd.charAt(i)<33||(int)passwd.charAt(i)>126)
                return false;
            if(passwd.charAt(i)>='A'&&passwd.charAt(i)<='Z')
                upper=1;
            else if(passwd.charAt(i)>='a'&&passwd.charAt(i)<='z')
                lower=1;
            else if(passwd.charAt(i)>='0'&&passwd.charAt(i)<='9')
                number=1;
            else
                other=1;
        }
        if((upper+lower+number+other)<2)
            return false;
        return true;
    }
    public void setPassword(String passwd){
        this.Password=passwd;
    }
    public String getPassword(){
        return this.Password;
    }
    public int chgpw(String a,String b){
        if(!checkPasswd(a))
            return -1;
        if(!a.equals(b))
            return -2;
        setPassword(a);
        return 1;
    }
}