package net.AzureWare.ui.altLogin;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

import javax.swing.JOptionPane;

import net.AzureWare.Client;
import net.AzureWare.ui.altLogin.AltLoginThread;
import net.AzureWare.ui.altLogin.PasswordField;
import net.AzureWare.utils.FlatColors;
import net.AzureWare.utils.Kuai8Tools;
import net.AzureWare.utils.TheAlten;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;

import org.lwjgl.input.Keyboard;

import com.thealtening.AltService;
import com.thealtening.AltService.EnumAltService;
import com.thealtening.utilities.SSLVerification;

public final class GuiAltLogin extends GuiScreen {
   private PasswordField password;
   private final GuiScreen previousScreen;
   private AltLoginThread thread;
   private GuiTextField username;
   private GuiButton loginButton;
   private GuiButton backButton;
   private GuiButton kuai8Button;
   private GuiButton lastkuai8Button;
   private GuiButton altenlogin;
   private GuiButton importbutton;
   private GuiButton altenRandom;
   public static boolean isAlten = false;
   public GuiAltLogin(GuiScreen previousScreen) {
      this.previousScreen = previousScreen;
   }

   protected void actionPerformed(GuiButton button) {
		
      switch(button.id) {
      case 0:
    	  if(isAlten) {
    		  this.thread = new AltLoginThread(this.username.getText(), "anything");
    	  }else {
    		  this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
    	  }
         this.thread.start();
         break;
      case 1:
         this.mc.displayGuiScreen(this.previousScreen);
         break;
      case 2:
         String data = null;
         try {
            data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
         } catch (Exception var4) {
            return;
         }

         if(data.contains(":")) {
            String[] credentials = data.split(":");
            this.username.setText(credentials[0]);
            this.password.setText(credentials[1]);
         }
         break;
      case 3:
          this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
          this.thread.start();
    	  if(Kuai8Tools.isKuai8MCRunning()) {
        	  Minecraft.getMinecraft().session = Kuai8Tools.getKuai8Session();
        	  this.thread.setStatus("Kuai8MC Login Successfully!\nUsername:" + Kuai8Tools.getUsername() + "\nServer IP:" + Kuai8Tools.getIP());
        	  Kuai8Tools.killKuai8MC();
    	  }else {
    		  this.thread.setStatus("Kuai8MC is not Running!");
    	  }
    	  break;
      case 4:
    	  this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
          this.thread.start();
          if(Kuai8Tools.getLastKuai8Session() == null) {
        	  this.thread.setStatus("No Login Session!");
          }else {
        	  this.thread.setStatus("Load Last Login Data Successfully!");
        	  Minecraft.getMinecraft().session = Kuai8Tools.getLastKuai8Session();
          }
          break;
      case 5:
    	  isAlten = true;
    	  	try {
    	  		this.mc.displayGuiScreen(this.previousScreen);
    			SSLVerification sslVerification = new SSLVerification();
    			sslVerification.verify();
    			AltService altService = new AltService();
    			altService.switchService(EnumAltService.THEALTENING); 
    			System.out.println(altService.getCurrentService().toString());
    		} catch (Exception e) {
    			// TODO 自动生成的 catch 块
    			e.printStackTrace();
    		}
    	  	break;
      case 6:
    	 if(TheAlten.getAPI() == null) {
    		 TheAlten.saveAPI(JOptionPane.showInputDialog( "你还没有设置Token，请输入你的Token："));
    	 }
    	 this.username.setText(TheAlten.getAlt(TheAlten.getAPI()).getEmail());
      }

   }

   public void drawScreen(int x2, int y2, float z2) {
      FontRenderer font = mc.fontRendererObj;
      this.drawDefaultBackground();
      this.username.drawTextBox();
      if(!isAlten)this.password.drawTextBox();
      if(isAlten) this.drawCenteredString(font, "The Altening API Mode Enabled! You can not use CL-API or Kuai8Login.", this.width / 2, 38, -1);
      this.drawCenteredString(font, "Alt Login", this.width / 2, 20, -1);
      this.drawCenteredString(font, this.thread == null?"Idle...":this.thread.getStatus(), this.width / 2, 29, -1);
      if(this.username.getText().isEmpty()) {
         this.drawString(font, "Username / E-Mail", this.width / 2 - 96, 66, -7829368);
      }

      if(!isAlten && this.password.getText().isEmpty()) {
    	  this.drawString(font, "Password", this.width / 2 - 96, 106, -7829368);
      }

      super.drawScreen(x2, y2, z2);
   }

   public void initGui() {
	   FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
      int var3 = this.height / 4 + 24;
      this.loginButton = new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, 65, 15, "Login");
      this.backButton = new GuiButton(1, this.width / 2 - 30, var3 + 72 + 12, 65, 15, "Back");
      this.importbutton = new GuiButton(2, this.width / 2 + 40, var3 + 72 + 12, 65, 15, "User:Pass");
      this.kuai8Button = new GuiButton(3, this.width / 2 - 100, var3 + 72 - 20, 65, 15, "Kuai8Login");
      this.lastkuai8Button = new GuiButton(4, this.width / 2 + 40, var3 + 72 - 20, 65, 15, "LastKuai8");
      this.altenlogin = new GuiButton(5, this.width / 2 - 30, var3 + 72 - 20, 65, 15, "TheAltening");
      this.altenRandom = new GuiButton(6, this.width / 2 - 30, var3 + 72 - 20, 65, 15, "Get an Alt");
      this.buttonList.add(this.loginButton);
      this.buttonList.add(this.backButton);
      this.buttonList.add(this.importbutton);
      if(!isAlten) this.buttonList.add(this.kuai8Button);
      if(!isAlten) this.buttonList.add(this.lastkuai8Button);
      if(!isAlten) this.buttonList.add(this.altenlogin);
      if(isAlten) this.buttonList.add(this.altenRandom);
      this.username = new GuiTextField(var3, font, this.width / 2 - 100, 60, 200, 20);
      if(!isAlten) this.password = new PasswordField(font, this.width / 2 - 100, 100, 200, 20);
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
         if(!this.username.isFocused() && (!isAlten && !this.password.isFocused())) {
            this.username.setFocused(true);
         } else {
            if(!isAlten) this.username.setFocused(this.password.isFocused());
            if(!isAlten) this.password.setFocused(!this.username.isFocused());
         }
      }

      if(character == 13) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      this.username.textboxKeyTyped(character, key);
      if(!isAlten) this.password.textboxKeyTyped(character, key);
   }

   protected void mouseClicked(int x2, int y2, int button) {
      try {
         super.mouseClicked(x2, y2, button);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.username.mouseClicked(x2, y2, button);
      if(!isAlten) this.password.mouseClicked(x2, y2, button);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
      this.username.updateCursorCounter();
      if(!isAlten) this.password.updateCursorCounter();
   }
}
