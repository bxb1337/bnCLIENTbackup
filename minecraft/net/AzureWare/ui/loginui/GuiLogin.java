package net.AzureWare.ui.loginui;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.AzureWare.Client;
import net.AzureWare.ui.altLogin.AltLoginThread;
import net.AzureWare.ui.altLogin.PasswordField;
import net.AzureWare.utils.FlatColors;
import net.AzureWare.utils.HwidTools;
import net.AzureWare.utils.Kuai8Tools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.main.Main;
import net.minecraft.util.Session;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.lwjgl.input.Keyboard;

public final class GuiLogin extends GuiScreen {
   private PasswordField password;
   private final GuiScreen previousScreen;
   
   private GuiTextField username;
   private GuiTextField invitecode;
   private GuiTextField email;
   
   private GuiButton loginButton;
   private GuiButton registerButton;

   public GuiLogin(GuiScreen previousScreen) {
      this.previousScreen = previousScreen;
   }

   public class Notice extends JFrame{
	    public Notice(String notice) {
	    	JLabel label = new JLabel(notice);
	        Button bt = new Button("确定");
	        bt.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if(notice.contains("成功！")) {
	            		System.exit(0);
	            	}else {
	            		setVisible(false);
	            	}
	                //如果第一个参数是null，那么弹出的消息窗口，很可能就被置顶的窗口JFrame挡住了
	                //第一个窗口bt.getParent()父组件就是本窗口了，也可以写bt那么父组件就是按钮，都可以显示出来，
	                //但位置不同。bt.getParent()位于界面中央，更美观一点吧
	            }
	        });
	        add(label);
	        add(bt);
	        setLayout(new FlowLayout());
	        setTitle("提示");
	        setSize(300, 80);
	        setLocationRelativeTo(null);//窗口居中
	        setAlwaysOnTop(true);//置顶
	        setResizable(false);//禁止缩放
	        setVisible(true);
	    }
	}
   

   public void drawScreen(int x2, int y2, float z2) {
	   System.exit(0);
      FontRenderer font = mc.fontRendererObj;
      this.drawDefaultBackground();
      this.username.drawTextBox();
      this.password.drawTextBox();
      
      this.email.drawTextBox();
      this.invitecode.drawTextBox();
      
      this.drawCenteredString(font, "你需要登录", this.width / 2, 20, -1);
      this.drawCenteredString(font, "你的HWID未被记录 禁止使用共享账号", this.width / 2, 30, -1);
      if(this.username.getText().isEmpty()) {
         this.drawString(font, "用户名", this.width / 2 - 96, 66, -7829368);
      }

      if(this.password.getText().isEmpty()) {
         this.drawString(font, "密码", this.width / 2 - 96, 106, -7829368);
      }
      
      if(this.email.getText().isEmpty()) {
         this.drawString(font, "邮箱(如果你需要注册的话请填写)", this.width / 2 - 96, 146, -7829368);
      }
      
      if(this.invitecode.getText().isEmpty()) {
         this.drawString(font, "注册码(如果你需要注册的话请填写)", this.width / 2 - 96, 186, -7829368);
      }

      super.drawScreen(x2, y2, z2);
   }

   public void initGui() {
	  FontRenderer font = mc.fontRendererObj;
      int var3 = this.height / 4 + 24;
      this.loginButton = new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12 + 40, 65, 20, "登录");
      this.registerButton = new GuiButton(1, this.width / 2 + 35, var3 + 72 + 12 + 40, 65, 20, "注册");
      this.buttonList.add(this.loginButton);
      this.buttonList.add(this.registerButton);

      this.username = new GuiTextField(var3, font, this.width / 2 - 100, 60, 200, 20);
      this.password = new PasswordField(font, this.width / 2 - 100, 100, 200, 20);
      this.email = new GuiTextField(var3, font, this.width / 2 - 100, 140, 200, 20);
      this.invitecode = new GuiTextField(var3, font, this.width / 2 - 100, 180, 200, 20);

      this.username.setFocused(true);
      
      Keyboard.enableRepeatEvents(true);
   }

   protected void keyTyped(char character, int key) {
      try {
         super.keyTyped(character, key);
      } catch (IOException var4) {
         var4.printStackTrace();
      }
      
      if(character == 9) {
         if(!this.username.isFocused() && !this.password.isFocused()) {
            this.username.setFocused(true);
         } else {
            this.username.setFocused(this.password.isFocused());
            this.password.setFocused(!this.username.isFocused());
         }
      }

      if(character == 13) {
      //   this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      if((int)character == 27) {
    	  System.exit(0);
      }
      
      this.username.textboxKeyTyped(character, key);
      this.password.textboxKeyTyped(character, key);
      this.invitecode.textboxKeyTyped(character, key);
      this.email.textboxKeyTyped(character, key);
   }

   protected void mouseClicked(int x2, int y2, int button) {
      try {
         super.mouseClicked(x2, y2, button);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.username.mouseClicked(x2, y2, button);
      this.password.mouseClicked(x2, y2, button);
      this.email.mouseClicked(x2, y2, button);
      this.invitecode.mouseClicked(x2, y2, button);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
      this.username.updateCursorCounter();
      this.password.updateCursorCounter();
      this.invitecode.updateCursorCounter();
      this.email.updateCursorCounter();
   }
}
