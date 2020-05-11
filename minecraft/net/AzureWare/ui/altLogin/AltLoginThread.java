package net.AzureWare.ui.altLogin;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AltService;
import com.thealtening.AltService.EnumAltService;
import com.thealtening.utilities.SSLVerification;

import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public final class AltLoginThread extends Thread {
   private final String password;
   private String status;
   private final String username;
   private Minecraft mc = Minecraft.getMinecraft();

   public AltLoginThread(String username, String password) {
      super("Alt Login Thread");
      this.username = username;
      this.password = password;
      this.status = "Waiting...";
   }

   private Session createSession(String username, String password) {
      YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
      auth.setUsername(username);
      auth.setPassword(password);

      try {
         auth.logIn();
         return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
      } catch (AuthenticationException var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public String getStatus() {
      return this.status;
   }

   public void run() {
	  if(this.password.equals("") && this.username.equals("")) {
		  System.out.println("Kuai8Login");
		  return;
	  }
	  
      if(this.password.equals("")) {
         this.mc.session = new Session(this.username, "", "", "mojang");
         this.status = "Logged in. (" + this.username + " - cracked name)";
      } else {
         this.status = "Logging in...";
         Session auth = this.createSession(this.username, this.password);
         if(auth == null) {
            this.status = "Login failed!";
         } else {
            this.status = "Logged in. (" + auth.getUsername() + " - Premium Account)";
            this.mc.session = auth;
         }
      }
   }

   public void setStatus(String status) {
      this.status = status;
   }
}
