import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

public class ClientUI {
    private static ClientUI self=new ClientUI();
    private boolean isConnect=false;
    private boolean isLogin=false;
    private boolean isRoomConnect=false;
    private JFrame frame;
    private JPanel loginPanel;
    private JPanel CEPanel;
    private JPanel roomPanel;

    private ClientUI(){
        frame = new JFrame("Login");
        frame.setSize(250, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginPanel=new LoginUI().getJp();
        CEPanel=new ConnectError().getCE();
        roomPanel=new RoomUI().getPanel();
        frame.add(CEPanel);
        frame.add(loginPanel);
        CEPanel.setVisible(false);
        loginPanel.setVisible(false);
    }
    public static ClientUI getClientUI(){
        return self;
    }
    public JFrame getFrame() {
        return frame;
    }
    public JPanel getLoginPanel() {
        return loginPanel;
    }
    public JPanel getCEPanel() {
        return CEPanel;
    }
    public JPanel getRoomPanel() {
        return roomPanel;
    }

    public boolean isConnect() {
        return isConnect;
    }
    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public boolean isLogin() {
        return isLogin;
    }
    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isRoomConnect() {
        return isRoomConnect;
    }
    public void setRoomConnect(boolean roomConnect) {
        isRoomConnect = roomConnect;
    }


    public static void main(String[] args){
        JFrame frame = new JFrame("Login");
        frame.setSize(250, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new RoomUI().getPanel());
        frame.setVisible(true);
    }
}
class LoginUI{
    private JPanel jp;
    private JLabel userLable;
    private JLabel passwordLable;
    private JTextField username;      // 用户名输入框
    private JPasswordField password;
    private JButton login;
    private JButton signup;

    public LoginUI(){
        jp = new JPanel();
        userLable = new JLabel("用户名:");
        passwordLable = new JLabel("密码:");
        username=new JTextField(10);
        password=new JPasswordField(15);
        login=new JButton("login");
        signup=new JButton("注册");
        jp.add(userLable);
        jp.add(passwordLable);
        jp.add(username);
        jp.add(password);
        jp.add(login);
        //jp.add(signup);

        jp.setLayout(null);
        jp.setPreferredSize(new Dimension(250, 300));
        userLable.setBounds(20,70,80,25);
        passwordLable.setBounds(25,120,80,25);
        username.setBounds(75,70,100,25);
        password.setBounds(75,120,100,25);
        login.setBounds(75,170,100,25);
        //signup.setBounds(150,170,50,25);

        MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                192));
        username.setBorder(border);
        password.setBorder(border);
        login.setEnabled(false);

        username.getDocument().addDocumentListener(new UserChangeListener());
        password.getDocument().addDocumentListener(new PasswordChangeListener());
        username.addKeyListener(new NameTyped());
        password.addKeyListener(new PassworTyped());
        login.addActionListener(new LoginListener());
    }
    public JPanel getJp(){
        return  jp;
    }
    class LoginListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String u=username.getText();
            String p=new String(password.getPassword());
            ClientSend s=ClientSend.getClientSend();
            ClientReceive r=ClientReceive.getClientReceive();
            synchronized (s){
                synchronized (r){
                    s.notify();
                    r.notify();
                    System.out.println(u);
                    System.out.println(p);
                    System.out.println("Login button pressed");
                    if(Client.login(username.getText(),new String(password.getPassword()),ClientSend.getClientSend(),ClientReceive.getClientReceive()))
                        System.out.println("login seccess");

                }
            }
        }
    }
    class UserChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            String u=username.getText();
            String p=new String(password.getPassword());
            boolean ub=checkName(u);
            boolean pb=checkPassword(p);
            if (ub){
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                        192));
                username.setBorder(border);
            }
            else{
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(200, 0,
                        0));
                username.setBorder(border);
            }
            if(ub&&pb)
                login.setEnabled(true);
            else
                login.setEnabled(false);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            String u=username.getText();
            String p=new String(password.getPassword());
            boolean ub=checkName(u);
            boolean pb=checkPassword(p);
            if (ub){
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                        192));
                username.setBorder(border);
            }
            else{
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(200, 0,
                        0));
                username.setBorder(border);
            }
            if(ub&&pb)
                login.setEnabled(true);
            else
                login.setEnabled(false);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }
    class PasswordChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            String u=username.getText();
            String p=new String(password.getPassword());
            boolean ub=checkName(u);
            boolean pb=checkPassword(p);
            if(pb){
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                        192));
                password.setBorder(border);
            }
            else{
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(200, 0,
                        0));
                password.setBorder(border);
            }
            if(ub&&pb)
                login.setEnabled(true);
            else
                login.setEnabled(false);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            String u=username.getText();
            String p=new String(password.getPassword());
            boolean ub=checkName(u);
            boolean pb=checkPassword(p);
            if(pb){
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                        192));
                password.setBorder(border);
            }
            else{
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(200, 0,
                        0));
                password.setBorder(border);
            }
            if(ub&&pb)
                login.setEnabled(true);
            else
                login.setEnabled(false);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }
    class NameTyped implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {
            String u=username.getText();
            if(u.length() >= 10) e.consume();
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
    class PassworTyped implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {
            String p=new String(password.getPassword());
            if(p.length() >= 15) e.consume();
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    static boolean checkName(String name){
        if(name.equals(""))
            return false;
        if(!Pattern.matches("^[A-Za-z0-9]+$",name))
            return false;
        return true;
    }
    static boolean checkPassword(String password){
        if(password.equals(""))
            return false;
        if(!Pattern.matches("^[A-Za-z0-9]+$",password))
            return false;
        return true;
    }
}
class ConnectError{
    private JPanel CE;
    private JLabel info;

    public ConnectError(){
        CE = new JPanel();
        info = new JLabel("网络错误");

        CE.setLayout(null);
        CE.setPreferredSize(new Dimension(250, 300));
        info.setBounds(85,100,80,25);

        CE.add(info);
    }
    public JPanel getCE(){
        return CE;
    }
}
class RoomUI{
    private JPanel panel;
    private JRadioButton RB01,RB02;
    private JRadioButton RB11,RB12;
    private ButtonGroup BG0;
    private ButtonGroup BG1;
    private JLabel RIDLable;
    private JLabel passwordLable;
    private JTextField RID;      // 用户名输入框
    private JPasswordField password;
    private JButton submit;
    public JPanel getPanel(){
        return panel;
    }
    public RoomUI(){
        panel=new JPanel();
        RB01=new JRadioButton("创建房间",true);
        RB02=new JRadioButton("假如房间");
        BG0=new ButtonGroup();
        RB11=new JRadioButton("　公开　",true);
        RB12=new JRadioButton("　私密　");
        BG1=new ButtonGroup();

        RIDLable = new JLabel("房间号:");
        passwordLable = new JLabel("密码:");
        RID=new JTextField(10);
        password=new JPasswordField(15);
        submit=new JButton("创建");
        BG0.add(RB01);BG0.add(RB02);
        BG1.add(RB11);BG1.add(RB12);
        panel.add(RB01);panel.add(RB02);
        panel.add(RB11);panel.add(RB12);
        panel.add(RID);panel.add(RIDLable);
        panel.add(password);panel.add(passwordLable);
        RID.setVisible(false);RIDLable.setVisible(false);
        password.setVisible(false);passwordLable.setVisible(false);
        panel.add(submit);

        panel.setLayout(null);
        RB01.setBounds(30,10,75,25);
        RB02.setBounds(125,10,75,25);
        RB11.setBounds(30,35,75,25);
        RB12.setBounds(125,35,75,25);

        panel.setPreferredSize(new Dimension(250, 300));
        RIDLable.setBounds(20,85,80,25);
        RID.setBounds(75,85,100,25);
        passwordLable.setBounds(25,120,80,25);
        password.setBounds(75,120,100,25);
        submit.setBounds(75,170,100,25);

        MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                192));
        RID.setBorder(border);
        password.setBorder(border);
        submit.setEnabled(true);

        RB01.addChangeListener(new RBChangeListener());
        RB11.addChangeListener(new RBChangeListener());
        RID.getDocument().addDocumentListener(new RIDChangeListener());
        password.getDocument().addDocumentListener(new PasswordChangeListener());
        RID.addKeyListener(new RIDTyped());
        password.addKeyListener(new PassworTyped());
        submit.addActionListener(new RoomListener());
    }
    class RoomListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String rid=RID.getText();
            String pass=new String(password.getPassword());
            ClientSend s=ClientSend.getClientSend();
            ClientReceive r=ClientReceive.getClientReceive();
            synchronized (s){
                synchronized (r){
                    s.notify();
                    r.notify();
                    System.out.println(rid);
                    System.out.println(pass);
                    System.out.println("Room button pressed");
                    int ret;
                    if(RB01.isSelected()){
                        if(RB11.isSelected()){
                            ret=Client.createRoom(false,null,s,r);
                        }
                        else {
                            ret=Client.createRoom(true,pass,s,r);
                        }
                        if(ret>=1000)
                            System.out.println("create room:"+ret);
                        else
                            System.out.println("crete room fail");
                    }
                    else {
                        if(RB11.isSelected()){
                            ret=Client.joinRoom(rid,s,r);
                        }
                        else {
                            ret=Client.joinRoom(rid,pass,s,r);
                        }
                        if(ret==1)
                            System.out.println("join room");
                        else if(ret==0)
                            System.out.println("room not found");
                        else if(ret==-1)
                            System.out.println("wrong password");
                    }
                }
            }
        }
    }
    class RBChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if(RB01.isSelected()){
                if(RB11.isSelected()){
                    RID.setVisible(false);
                    RIDLable.setVisible(false);
                    password.setVisible(false);
                    passwordLable.setVisible(false);
                }
                else {
                    RID.setVisible(false);
                    RIDLable.setVisible(false);
                    password.setVisible(true);
                    passwordLable.setVisible(true);
                }
                submit.setText("create");
            }
            else {
                if(RB11.isSelected()){
                    RID.setVisible(true);
                    RIDLable.setVisible(true);
                    password.setVisible(false);
                    passwordLable.setVisible(false);
                }
                else{
                    RID.setVisible(true);
                    RIDLable.setVisible(true);
                    password.setVisible(true);
                    passwordLable.setVisible(true);
                }
                submit.setText("join");
            }

            String r=RID.getText();
            String p=new String(password.getPassword());
            boolean rb=checkRID(r);
            boolean pb=checkPassword(p);
            if(RB01.isSelected()){
                if(RB11.isSelected()){
                    submit.setEnabled(true);
                }
                else {
                    if(pb)
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);
                }
            }
            else {
                if(RB11.isSelected()){
                    if(rb)
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);
                }
                else {
                    if ((rb&&pb))
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);

                }
            }
        }
    }
    class RIDChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            String r=RID.getText();
            String p=new String(password.getPassword());
            boolean rb=checkRID(r);
            boolean pb=checkPassword(p);
            if (rb){
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                        192));
                RID.setBorder(border);
            }
            else{
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(200, 0,
                        0));
                RID.setBorder(border);
            }
            if(RB01.isSelected()){
                if(RB11.isSelected()){
                    submit.setEnabled(true);
                }
                else {
                    if(pb)
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);
                }
            }
            else {
                if(RB11.isSelected()){
                    if(rb)
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);
                }
                else {
                    if ((rb&&pb))
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);

                }
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            String r=RID.getText();
            String p=new String(password.getPassword());
            boolean rb=checkRID(r);
            boolean pb=checkPassword(p);
            if (rb){
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                        192));
                RID.setBorder(border);
            }
            else{
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(200, 0,
                        0));
                RID.setBorder(border);
            }
            if(RB01.isSelected()){
                if(RB11.isSelected()){
                    submit.setEnabled(true);
                }
                else {
                    if(pb)
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);
                }
            }
            else {
                if(RB11.isSelected()){
                    if(rb)
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);
                }
                else {
                    if ((rb&&pb))
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);

                }
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }
    class PasswordChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            String r=RID.getText();
            String p=new String(password.getPassword());
            boolean rb=checkRID(r);
            boolean pb=checkPassword(p);
            if (pb){
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                        192));
                password.setBorder(border);
            }
            else{
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(200, 0,
                        0));
                password.setBorder(border);
            }
            if(RB01.isSelected()){
                if(RB11.isSelected()){
                    submit.setEnabled(true);
                }
                else {
                    if(pb)
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);
                }
            }
            else {
                if(RB11.isSelected()){
                    if(rb)
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);
                }
                else {
                    if ((rb&&pb))
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);

                }
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            String r=RID.getText();
            String p=new String(password.getPassword());
            boolean rb=checkRID(r);
            boolean pb=checkPassword(p);
            if (pb){
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                        192));
                password.setBorder(border);
            }
            else{
                MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(200, 0,
                        0));
                password.setBorder(border);
            }
            if(RB01.isSelected()){
                if(RB11.isSelected()){
                    submit.setEnabled(true);
                }
                else {
                    if(pb)
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);
                }
            }
            else {
                if(RB11.isSelected()){
                    if(rb)
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);
                }
                else {
                    if ((rb&&pb))
                        submit.setEnabled(true);
                    else
                        submit.setEnabled(false);

                }
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }
    class RIDTyped implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {
            String u=RID.getText();
            if(u.length() >= 4) e.consume();
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
    class PassworTyped implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {
            String p=new String(password.getPassword());
            if(p.length() >= 15) e.consume();
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
    static boolean checkRID(String rid){
        if(rid.length()!=4)
            return false;
        if (!Pattern.matches("^[0-9]+$",rid))
            return false;
        return true;
    }
    static boolean checkPassword(String password){
        if(password.equals(""))
            return false;
        if(!Pattern.matches("^[A-Za-z0-9]+$",password))
            return false;
        return true;
    }
}
class ChatUI{}