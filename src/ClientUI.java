import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class ClientUI {
    private static ClientUI self=new ClientUI();
    private boolean isConnect=false;
    private boolean isLogin=false;
    private boolean isRoomConnect=false;
    private boolean isAdmin=false;
    private int RID=0;
    private String name=null;
    private JFrame frame;
    private JPanel loginPanel;
    private JPanel CEPanel;
    private JPanel roomPanel;
    private JPanel signupsuccessPanel;
    private JPanel signupfailPanel;
    private JPanel loginfailPanel;
    private JPanel chatroomPanel;

    private ChatUI chat;
    private UserListUI user;

    private ClientUI() {
        frame = new JFrame("Login");
        frame.setSize(250, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginPanel = new LoginUI().getJp();
        CEPanel = new ConnectError().getCE();
        roomPanel = new RoomUI().getPanel();
        signupsuccessPanel=new SignupSuccessUI().getJp();
        signupfailPanel=new SignupFailUI().getJp();
        loginfailPanel=new LoginFailUI().getJp();
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
    public JPanel getChatRoomPanel() {
        return chatroomPanel;
    }
    public ChatUI getChat() {
        return chat;
    }
    public UserListUI getUser() {
        return user;
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

    public String getName(){return this.name;};
    public void setName(String name){
        this.name=name;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    public void init2CE(){
        frame.add(CEPanel);
    }
    public void CE2Login(){
        frame.remove(CEPanel);
        frame.add(loginPanel);
        loginPanel.revalidate();
        roomPanel.updateUI();
        setConnect(true);
    }
    public void Login2Room(){
        frame.remove(loginPanel);
        frame.add(roomPanel);
        roomPanel.revalidate();
        roomPanel.updateUI();
        setLogin(true);
    }
    public void Login2LoginFail(){
        frame.remove(loginPanel);
        frame.add(loginfailPanel);
        loginfailPanel.revalidate();
        loginfailPanel.updateUI();
        setLogin(true);
    }
    public void Login2SignupSuccess(){
        frame.remove(loginPanel);
        frame.add(signupsuccessPanel);
        signupsuccessPanel.revalidate();
        signupsuccessPanel.updateUI();
        setLogin(true);
    }
    public void Login2SignupFail(){
        frame.remove(loginPanel);
        frame.add(signupfailPanel);
        signupfailPanel.revalidate();
        signupfailPanel.updateUI();
        setLogin(true);
    }
    public void SignupSuccess2Login(){
    	frame.remove(signupsuccessPanel);
        frame.add(loginPanel);
        loginPanel.revalidate();
        loginPanel.updateUI();
        setLogin(false);
    }
    public void SignupFail2Login(){
    	frame.remove(signupfailPanel);
        frame.add(loginPanel);
        loginPanel.revalidate();
        loginPanel.updateUI();
        setLogin(false);
    }
    public void LoginFail2Login(){
    	frame.remove(loginfailPanel);
        frame.add(loginPanel);
        loginPanel.revalidate();
        loginPanel.updateUI();
        setLogin(false);
    }
    public void Room2Chat(int rid){
        frame.remove(roomPanel);
        frame.setSize(810,600);
        ChatRoomUI c=new ChatRoomUI(rid,isAdmin,Client.getUserList(name,ClientSend.getClientSend(),ClientReceive.getClientReceive()));
        chatroomPanel=c.getPanel();
        chat=c.getChatUI();
        user=c.getUserListUI();
        frame.add(chatroomPanel);

        chatroomPanel.revalidate();
        chatroomPanel.updateUI();
        RID=rid;
        setRoomConnect(true);
    }
    public void Chat2Room(){
        System.out.println("Chat2Room");
        frame.remove(chatroomPanel);
        frame.setSize(250,300);
        roomPanel=new RoomUI().getPanel();
        frame.add(roomPanel);
        roomPanel.revalidate();
        roomPanel.updateUI();

        ClientReceive r=ClientReceive.getClientReceive();
        ClientSend s= ClientSend.getClientSend();
        synchronized (s){
            synchronized (r){
                s.notify();
                r.notify();
                r.setChat(false);
                s.setChat(false);
                r.setRoom(false);
                s.setRoom(false);
            }
        }
        setRoomConnect(false);
        setLogin(true);
    }
    public static void main(String[] args){
        JFrame frame = new JFrame("Login");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[][] t = {{"A1"}, {"A2"}, {"A3"}};
        JPanel jp=new ChatRoomUI(1923,false,t).getPanel();

        frame.add(jp);
        frame.setVisible(true);
    }
}
class SignupSuccessUI{
    private JPanel jp=new JPanel();
    private JLabel Lable= new JLabel("注册成功");
    private JButton back=new JButton("返回登录页面");

    public SignupSuccessUI(){
        jp.add(Lable);
        jp.add(back);
        
        jp.setLayout(null);
        jp.setPreferredSize(new Dimension(250, 300));
        Lable.setBounds(95,80,150,25);
        back.setBounds(63,140,120,25);
        
        back.addActionListener(new BackListener());
    }
    public JPanel getJp(){
        return  jp;
    }
    class BackListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        	System.out.println("Back button pressed");
            ClientUI.getClientUI().SignupSuccess2Login();
            System.out.println("Back seccess");
        }
    }
}
class SignupFailUI{
    private JPanel jp=new JPanel();
    private JLabel Lable= new JLabel("注册失败");
    private JButton back=new JButton("返回登录页面");

    public SignupFailUI(){
        jp.add(Lable);
        jp.add(back);
        
        jp.setLayout(null);
        jp.setPreferredSize(new Dimension(250, 300));
        Lable.setBounds(95,80,150,25);
        back.setBounds(63,140,120,25);
        
        back.addActionListener(new BackListener());
    }
    public JPanel getJp(){
        return  jp;
    }
    class BackListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        	System.out.println("Back button pressed");
            ClientUI.getClientUI().SignupFail2Login();
            System.out.println("Back seccess");
        }
    }
}
class LoginFailUI{
    private JPanel jp=new JPanel();
    private JLabel Lable= new JLabel("登录失败");
    private JButton back=new JButton("返回登录页面");

    public LoginFailUI(){
        jp.add(Lable);
        jp.add(back);
        
        jp.setLayout(null);
        jp.setPreferredSize(new Dimension(250, 300));
        Lable.setBounds(95,80,150,25);
        back.setBounds(63,140,120,25);
        
        back.addActionListener(new BackListener());
    }
    public JPanel getJp(){
        return  jp;
    }
    class BackListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        	System.out.println("Back button pressed");
            ClientUI.getClientUI().LoginFail2Login();
            System.out.println("Back seccess");
        }
    }
}
class LoginUI{
    private JPanel jp=new JPanel();
    private JLabel userLable= new JLabel("用户名:");
    private JLabel passwordLable= new JLabel("密码:");
    private JTextField username=new JTextField(10);      // 用户名输入框
    private JPasswordField password=new JPasswordField(15);
    private JButton login=new JButton("login");
    private JButton signup=new JButton("signup");

    public LoginUI(){
        jp.add(userLable);
        jp.add(passwordLable);
        jp.add(username);
        jp.add(password);
        jp.add(login);
        jp.add(signup);

        jp.setLayout(null);
        jp.setPreferredSize(new Dimension(250, 300));
        userLable.setBounds(20,70,80,25);
        passwordLable.setBounds(25,120,80,25);
        username.setBounds(75,70,100,25);
        password.setBounds(75,120,100,25);
        login.setBounds(35,170,80,25);
        signup.setBounds(135,170,80,25);

        MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(192, 192,
                192));
        username.setBorder(border);
        password.setBorder(border);
        login.setEnabled(false);
        signup.setEnabled(false);

        username.getDocument().addDocumentListener(new UserChangeListener());
        password.getDocument().addDocumentListener(new PasswordChangeListener());
        username.addKeyListener(new NameTyped());
        password.addKeyListener(new PassworTyped());
        login.addActionListener(new LoginListener());
        signup.addActionListener(new SignupListener());
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
                    if(Client.login(username.getText(),new String(password.getPassword()),ClientSend.getClientSend(),ClientReceive.getClientReceive())){
                        ClientUI.getClientUI().Login2Room();
                        ClientUI.getClientUI().setName(u);
                        System.out.println("login seccess");
                    }
                    else
                    {
                    	ClientUI.getClientUI().Login2LoginFail();
                        System.out.println("login fail");
                    }


                }
            }
        }
    }
    class SignupListener implements ActionListener{
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
                    System.out.println("Signup button pressed");
                    if(Client.signup(username.getText(),new String(password.getPassword()),ClientSend.getClientSend(),ClientReceive.getClientReceive())){
                        ClientUI.getClientUI().Login2SignupSuccess();
                        System.out.println("signup seccess");
                    }
                    else
                    {
                    	ClientUI.getClientUI().Login2SignupFail();
                        System.out.println("signup fail");
                    }


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
            {
            	login.setEnabled(true);
            	signup.setEnabled(true);
            }
            else
            {
            	login.setEnabled(false);
            	signup.setEnabled(false);
            }
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
            {
            	login.setEnabled(true);
            	signup.setEnabled(true);
            }
            else
            {
            	login.setEnabled(false);
            	signup.setEnabled(false);
            }
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
            {
            	login.setEnabled(true);
            	signup.setEnabled(true);
            }
            else
            {
            	login.setEnabled(false);
            	signup.setEnabled(false);
            }
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
            {
            	login.setEnabled(true);
            	signup.setEnabled(true);
            }
            else
            {
            	login.setEnabled(false);
            	signup.setEnabled(false);
            }
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
    private JPanel CE= new JPanel();
    private JLabel info= new JLabel("网络错误");

    public ConnectError(){
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
    private JPanel panel=new JPanel();
    private JRadioButton RB01=new JRadioButton("创建房间",true),RB02=new JRadioButton("加入房间");
    private JRadioButton RB11=new JRadioButton("　公开　",true),RB12=new JRadioButton("　私密　");
    private ButtonGroup BG0=new ButtonGroup();
    private ButtonGroup BG1=new ButtonGroup();
    private JLabel RIDLable= new JLabel("房间号:");
    private JLabel passwordLable= new JLabel("密码:");
    private JTextField RID=new JTextField(10);      // 用户名输入框
    private JPasswordField password=new JPasswordField(15);
    private JButton submit=new JButton("创建");
    public JPanel getPanel(){
        return panel;
    }
    public RoomUI(){
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
        RB01.setBounds(30,10,100,25);
        RB02.setBounds(150,10,100,25);
        RB11.setBounds(30,35,100,25);
        RB12.setBounds(150,35,100,25);

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
                        if(ret>=1000){
                            System.out.println("create room:"+ret);
                            ClientUI.getClientUI().setAdmin(true);
                            ClientUI.getClientUI().Room2Chat(ret);
                        }

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
                        if(ret==1){
                            ClientUI.getClientUI().Room2Chat(Integer.parseInt(rid));
                            System.out.println("join room");
                        }
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
class ChatRoomUI{
    ChatUI chat=new ChatUI();
    UserListUI user;

    JPanel panel=new JPanel();
    JPanel chatpanel=chat.getJp();
    JPanel userpanel;
    public ChatRoomUI(int rid,boolean isAdmin,String[][] users){
        user=new UserListUI(rid,isAdmin,users);
        userpanel=user.getPanel();
        chatpanel.setBounds(0,0,540,600);
        userpanel.setBounds(550,0,225,600);

        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(810, 600));
        panel.add(chatpanel);
        panel.add(userpanel);
    }
    public ChatUI getChatUI(){
        return chat;
    }
    public UserListUI getUserListUI(){
        return user;
    }
    public JPanel getPanel(){
        return panel;
    }
    public JPanel getChat(){
        return chatpanel;
    }
    public JPanel getUser(){
        return userpanel;
    }
}
class ChatUI{
    JPanel jp=new JPanel();
	// 创建文本区域组件
    JTextArea outputarea = new JTextArea();
    JTextArea inputarea = new JTextArea();
    //最底层面板，包含其他面板

    // 创建滚动面板，包括文本区域组件
    JScrollPane output = new JScrollPane(
    		outputarea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    JScrollPane input = new JScrollPane(
    		inputarea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    JButton send=new JButton("send");
    JLayeredPane layer = new JLayeredPane();
    //返回最外层panel
    public JPanel getJp(){
        return  jp;
    }
    public ChatUI(){
    	outputarea.setLineWrap(true);
    	outputarea.setEditable(false);
    	inputarea.setLineWrap(true);
    	inputarea.setEditable(true);
    	jp.add(output);
    	send.setEnabled(false);

        layer.setBounds(10,410,530,140);
        layer.add(inputarea,1);
        layer.add(send,0);
        jp.add(layer);

    	jp.setLayout(null);
        jp.setPreferredSize(new Dimension(550, 600));
    	output.setBounds(10,10,530,390);
    	inputarea.setBounds(0,0,520,130);
    	inputarea.setBorder(new MatteBorder(1, 1, 1, 1, new Color(122,138,153)));
    	send.setBounds(440,100,75,25);

        inputarea.getDocument().addDocumentListener(new InputChangeListener());
        inputarea.addKeyListener(new InputTyped());
        send.addActionListener(new SendListener());
    }
    class InputChangeListener implements DocumentListener{

        @Override
        public void insertUpdate(DocumentEvent e) {
            String input=inputarea.getText();
            if(input.length()==0)send.setEnabled(false);
            else send.setEnabled(true);
            send.repaint();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            String input=inputarea.getText();
            if(input.length()==0)send.setEnabled(false);
            else send.setEnabled(true);
            send.repaint();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            send.repaint();
        }
    }
    class InputTyped implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {
            String input=inputarea.getText();
            if(input.length() >= 200) e.consume();
            layer.repaint();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.isShiftDown()&&!e.isAltDown()&&!e.isControlDown()&&e.getKeyCode() == KeyEvent.VK_ENTER){
                inputarea.append("\n");
            }
            else if(!e.isShiftDown()&&!e.isAltDown()&&!e.isControlDown()&&e.getKeyCode() == KeyEvent.VK_ENTER){
                if(inputarea.getText().length()!=0){
                    sendButton();
                }
                else
                    e.consume();
            }
            //layer.repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(!e.isShiftDown()&&!e.isAltDown()&&!e.isControlDown()&&e.getKeyCode() == KeyEvent.VK_ENTER){
                inputarea.setText("");
            }
            //send.repaint();
        }
    }
    class SendListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            sendButton();
        }
    }
    public void sendButton(){
        String input=inputarea.getText();
        ClientSend s=ClientSend.getClientSend();
        synchronized (s){
            s.notify();
            s.send(input);
            inputarea.setText("");
            outputarea.setCaretPosition(outputarea.getDocument().getLength());
        }
    }
    //接受字符串
    public boolean addstring(String str)
    {
    	this.outputarea.append(str);
    	return true;
    }
}
class UserListUI {
    private static UserListUI self=null;
    private JPanel panel=new JPanel();
    private JLabel RID;
    private DefaultTableModel model=null;
    private JTable list;
    private JScrollPane scroll;
    private JButton close=new JButton("close");
    private JButton leave=new JButton("leave");
    private JPOP Jpop;
    private JPopupMenu pop ;

    public JPanel getPanel(){
        return panel;
    }
    public static UserListUI getSelf(){
        return self;
    }

    public UserListUI(int rid,boolean isAdmin,String[][] users) {
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(240, 600));


        RID=new JLabel("当前房间号:"+String.valueOf(rid));
        model=new DefaultTableModel(users, new String[]{"在线用户"}){public boolean isCellEditable(int row, int column) {return false;}};
        list=new JTable(model);
        scroll = new JScrollPane(list);
        list.setPreferredScrollableViewportSize(new Dimension(225, 450));
        list.setRowHeight(30);
        list.getTableHeader().setReorderingAllowed( false );
        scroll.setAutoscrolls(true);
        scroll.setBounds(0,50,225,450);

        RID.setBounds(70,0,120,50);
        leave.setBounds(20,510,75,25);
        close.setBounds(125,510,75,25);
        close.setEnabled(isAdmin);

        panel.add(scroll);
        panel.add(RID);
        panel.add(close);
        panel.add(leave);

        close.addActionListener(new CloseListener());
        leave.addActionListener(new LeaveListener());
        Jpop=new JPOP(isAdmin);
        pop=Jpop.pop;
        list.addMouseListener(new TableMouse());
        //list.setComponentPopupMenu(pop);
        self=this;
    }
    public void updataList(String[][] newlist){
        if(model!=null)
            model.setDataVector(newlist, new String[]{"在线用户"});
    }
    class JPOP{
        JPopupMenu pop = new JPopupMenu();// 弹出菜单对象
        JMenuItem mi_admin = new JMenuItem("设为管理员");
        JMenuItem mi_send=new JMenuItem("私信");
        JMenuItem mi_del= new JMenuItem("移除");
        boolean isAdmin;
        public JPOP(boolean isAdmin){
            this.isAdmin=isAdmin;
            mi_admin.setActionCommand("admin");
            mi_send.setActionCommand("send");
            mi_del.setActionCommand("del");
            ActionListener al = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    // 哪个菜单项点击了，这个s就是其设定的ActionCommand
                    popMenuAction(s);
                }
            };
            mi_admin.addActionListener(al);
            mi_send.addActionListener(al);
            mi_del.addActionListener(al);
            mi_del.setEnabled(isAdmin);
            pop.add(mi_send);
            pop.add(mi_del);
        }
        public void update(){
            if(list.getSelectedRow()!=-1&&list.getValueAt(list.getSelectedRow(),0).equals(ClientUI.getClientUI().getName())){
                mi_send.setEnabled(false);
                mi_del.setEnabled(false);
            }
            else if(isAdmin){
                mi_send.setEnabled(true);
                mi_del.setEnabled(true);
            }
            else{
                mi_send.setEnabled(true);
                mi_del.setEnabled(false);
            }
        }
    }
    private void popMenuAction(String command) {
        // 得到在表格上选中的行
        final int selectIndex = list.getSelectedRow();
        if (selectIndex == -1) {
            return;
        }
        if (command.equals("del")) {
            ClientSend s=ClientSend.getClientSend();
            synchronized (s){
                s.notify();
                Client.deleteUser((String) list.getValueAt(list.getSelectedRow(),0),s);
            }
        }
        else if (command.equals("send")) {
            JTextArea in=ClientUI.getClientUI().getChat().inputarea;
            String str=in.getText();
            if(str.startsWith("@"))
                str=str.substring(str.indexOf(':')+1);
            in.setText("@"+(String) list.getValueAt(list.getSelectedRow(),0)+":"+str);
        }
        else {
            System.out.println("??");
        }
        // 刷新表格
        //SwingUtilities.updateComponentTreeUI(list);
    }
    class TableMouse extends MouseAdapter {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                //通过点击位置找到点击为表格中的行
                int focusedRowIndex = list.rowAtPoint(evt.getPoint());
                if (focusedRowIndex == -1) {
                    return;
                }
                //将表格所选项设为当前右键点击的行
                list.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                Jpop.update();
                //弹出菜单
                pop.show(list, evt.getX(), evt.getY());
            }
            else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                //通过点击位置找到点击为表格中的行
                int focusedRowIndex = list.rowAtPoint(evt.getPoint());
                if (focusedRowIndex == -1) {
                    return;
                }
                //将表格所选项设为当前右键点击的行
                list.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                Jpop.update();
            }
        }
    }
    class CloseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            ClientSend s=ClientSend.getClientSend();
            synchronized (s){
                s.notify();
                s.send("-shutdown");
            }
        }
    }
    class LeaveListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            ClientSend s=ClientSend.getClientSend();
            synchronized (s){
                s.notify();
                s.send("-leave");
            }
        }
    }
}