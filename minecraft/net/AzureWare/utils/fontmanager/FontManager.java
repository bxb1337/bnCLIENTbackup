package net.AzureWare.utils.fontmanager;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;

public class FontManager {
   private HashMap fonts = new HashMap();
  // public UnicodeFontRenderer lemonMilkBold75 = this.getFont("LemonMilkbold", 75.0F, true);
   public UnicodeFontRenderer lemonMilkBold60 = this.getFont("LemonMilkbold", 60.0F, true);
   public UnicodeFontRenderer lemonMilkBold75 = this.getFont("LemonMilkbold", 75.0F, true);
   public UnicodeFontRenderer lemonMilkBold80 = this.getFont("LemonMilkbold", 80.0F, true);
   public UnicodeFontRenderer lemonMilkBold90 = this.getFont("LemonMilkbold", 90.0F, true);
   public UnicodeFontRenderer lemonMilkBold100 = this.getFont("LemonMilkbold", 100.0F, true);
   public UnicodeFontRenderer lemonMilkBold110 = this.getFont("LemonMilkbold", 110.0F, true);
   public UnicodeFontRenderer lemonMilkBold120 = this.getFont("LemonMilkbold", 120.0F, true);
   public UnicodeFontRenderer lemonMilk60 = this.getFont("LemonMilk", 60.0F, true);
   public UnicodeFontRenderer lemonMilk75 = this.getFont("LemonMilk", 75.0F, true);
   public UnicodeFontRenderer lemonMilk80 = this.getFont("LemonMilk", 80.0F, true);
   public UnicodeFontRenderer lemonMilk90 = this.getFont("LemonMilk", 90.0F, true);
   public UnicodeFontRenderer lemonMilk100 = this.getFont("LemonMilk", 100.0F, true);
   public UnicodeFontRenderer lemonMilk110 = this.getFont("LemonMilk", 110.0F, true);
   public UnicodeFontRenderer lemonMilk120 = this.getFont("LemonMilk", 120.0F, true);
   public UnicodeFontRenderer lemonMilklight60 = this.getFont("LemonMilklight", 60.0F, true);
   public UnicodeFontRenderer lemonMilklight75 = this.getFont("LemonMilklight", 75.0F, true);
   public UnicodeFontRenderer lemonMilkLight80 = this.getFont("LemonMilklight", 80.0F, true);
   public UnicodeFontRenderer lemonMilkLight90 = this.getFont("LemonMilklight", 90.0F, true);
   public UnicodeFontRenderer lemonMilkLight100 = this.getFont("LemonMilklight", 100.0F, true);
   public UnicodeFontRenderer lemonMilkLight110 = this.getFont("LemonMilklight", 110.0F, true);
   public UnicodeFontRenderer lemonMilkLight120 = this.getFont("LemonMilklight", 120.0F, true);
  // public UnicodeFontRenderer lemonMilkBold18 = this.getFont("LemonMilkbold", 18.0F);
   public UnicodeFontRenderer jellolight10 = this.getFont("jellolight", 10.0F);
   public UnicodeFontRenderer jellolight11 = this.getFont("jellolight", 11.0F);
   public UnicodeFontRenderer jellolight12 = this.getFont("jellolight", 12.0F);
   public UnicodeFontRenderer jellolight13 = this.getFont("jellolight", 13.0F);
   public UnicodeFontRenderer jellolight15 = this.getFont("jellolight", 15.0F);
   public UnicodeFontRenderer jellolight16 = this.getFont("jellolight", 16.0F);
   public UnicodeFontRenderer jellolight17 = this.getFont("jellolight", 17.0F);
   public UnicodeFontRenderer jellolight18 = this.getFont("jellolight", 18.0F);
   public UnicodeFontRenderer jellolight19 = this.getFont("jellolight", 19.0F);

   public UnicodeFontRenderer jellolight20 = this.getFont("jellolight", 20.0F);
   public UnicodeFontRenderer jellolight25 = this.getFont("jellolight", 25.0F);
   public UnicodeFontRenderer jellolight30 = this.getFont("jellolight", 30.0F);
   public UnicodeFontRenderer jellolight35 = this.getFont("jellolight", 35.0F);
   public UnicodeFontRenderer jellolight40 = this.getFont("jellolight", 40.0F);
   public UnicodeFontRenderer jellolight45 = this.getFont("jellolight", 45.0F);
   public UnicodeFontRenderer jellolight50 = this.getFont("jellolight", 50.0F);
   public UnicodeFontRenderer jellolight70 = this.getFont("jellolight", 70.0F);
   public UnicodeFontRenderer comfortaa10 = this.getFont("comfortaa", 10.0F);
   public UnicodeFontRenderer comfortaa11 = this.getFont("comfortaa", 11.0F);
   public UnicodeFontRenderer comfortaa12 = this.getFont("comfortaa", 12.0F);
   public UnicodeFontRenderer comfortaa13 = this.getFont("comfortaa", 13.0F);
   public UnicodeFontRenderer comfortaa15 = this.getFont("comfortaa", 15.0F);
   public UnicodeFontRenderer comfortaa16 = this.getFont("comfortaa", 16.0F);
   public UnicodeFontRenderer comfortaa17 = this.getFont("comfortaa", 17.0F);
   public UnicodeFontRenderer comfortaa18 = this.getFont("comfortaa", 18.0F);
   public UnicodeFontRenderer comfortaa20 = this.getFont("comfortaa", 20.0F);
   public UnicodeFontRenderer comfortaa25 = this.getFont("comfortaa", 25.0F);
   public UnicodeFontRenderer comfortaa30 = this.getFont("comfortaa", 30.0F);
   public UnicodeFontRenderer comfortaa35 = this.getFont("comfortaa", 35.0F);
   public UnicodeFontRenderer comfortaa40 = this.getFont("comfortaa", 40.0F);
   public UnicodeFontRenderer comfortaa45 = this.getFont("comfortaa", 45.0F);
   public UnicodeFontRenderer comfortaa50 = this.getFont("comfortaa", 50.0F);
   public UnicodeFontRenderer comfortaa70 = this.getFont("comfortaa", 70.0F);
   public UnicodeFontRenderer simpleton10 = this.getFont("simpleton", 10.0F, true);
   public UnicodeFontRenderer simpleton11 = this.getFont("simpleton", 11.0F, true);
   public UnicodeFontRenderer simpleton12 = this.getFont("simpleton", 12.0F, true);
   public UnicodeFontRenderer simpleton13 = this.getFont("simpleton", 13.0F, true);
   public UnicodeFontRenderer simpleton15 = this.getFont("simpleton", 15.0F, true);
   public UnicodeFontRenderer simpleton16 = this.getFont("simpleton", 16.0F, true);
   public UnicodeFontRenderer simpleton17 = this.getFont("simpleton", 17.0F, true);
   public UnicodeFontRenderer simpleton18 = this.getFont("simpleton", 18.0F, true);
   public UnicodeFontRenderer simpleton20 = this.getFont("simpleton", 20.0F, true);
   public UnicodeFontRenderer simpleton25 = this.getFont("simpleton", 25.0F, true);
   public UnicodeFontRenderer simpleton30 = this.getFont("simpleton", 30.0F, true);
   public UnicodeFontRenderer simpleton35 = this.getFont("simpleton", 35.0F, true);
   public UnicodeFontRenderer simpleton40 = this.getFont("simpleton", 40.0F, true);
   public UnicodeFontRenderer simpleton45 = this.getFont("simpleton", 45.0F, true);
   public UnicodeFontRenderer simpleton50 = this.getFont("simpleton", 50.0F, true);
   public UnicodeFontRenderer simpleton70 = this.getFont("simpleton", 70.0F, true);
   public UnicodeFontRenderer payback10 = this.getFont("payback", 10.0F);
   public UnicodeFontRenderer payback11 = this.getFont("payback", 11.0F);
   public UnicodeFontRenderer payback12 = this.getFont("payback", 12.0F);
   public UnicodeFontRenderer payback13 = this.getFont("payback", 13.0F);
   public UnicodeFontRenderer payback15 = this.getFont("payback", 15.0F);
   public UnicodeFontRenderer payback16 = this.getFont("payback", 16.0F);
   public UnicodeFontRenderer payback17 = this.getFont("payback", 17.0F);
   public UnicodeFontRenderer payback18 = this.getFont("payback", 18.0F);
   public UnicodeFontRenderer payback20 = this.getFont("payback", 20.0F);
   public UnicodeFontRenderer payback25 = this.getFont("payback", 25.0F);
   public UnicodeFontRenderer payback30 = this.getFont("payback", 30.0F);
   public UnicodeFontRenderer payback35 = this.getFont("payback", 35.0F);
   public UnicodeFontRenderer payback40 = this.getFont("payback", 40.0F);
   public UnicodeFontRenderer payback45 = this.getFont("payback", 45.0F);
   public UnicodeFontRenderer payback50 = this.getFont("payback", 50.0F);
   public UnicodeFontRenderer payback70 = this.getFont("payback", 70.0F);
   public UnicodeFontRenderer VERDANA10 = this.getFont("VERDANA", 10.0F);
   public UnicodeFontRenderer VERDANA11 = this.getFont("VERDANA", 11.0F);
   public UnicodeFontRenderer VERDANA12 = this.getFont("VERDANA", 12.0F);
   public UnicodeFontRenderer VERDANA13 = this.getFont("VERDANA", 13.0F);
   public UnicodeFontRenderer VERDANA15 = this.getFont("VERDANA", 15.0F);
   public UnicodeFontRenderer VERDANA16 = this.getFont("VERDANA", 16.0F);
   public UnicodeFontRenderer VERDANA17 = this.getFont("VERDANA", 17.0F);
   public UnicodeFontRenderer VERDANA18 = this.getFont("VERDANA", 18.0F);
   public UnicodeFontRenderer VERDANA20 = this.getFont("VERDANA", 20.0F);
   public UnicodeFontRenderer VERDANA25 = this.getFont("VERDANA", 25.0F);
   public UnicodeFontRenderer VERDANA30 = this.getFont("VERDANA", 30.0F);
   public UnicodeFontRenderer VERDANA35 = this.getFont("VERDANA", 35.0F);
   public UnicodeFontRenderer VERDANA40 = this.getFont("VERDANA", 40.0F);
   public UnicodeFontRenderer VERDANA45 = this.getFont("VERDANA", 45.0F);
   public UnicodeFontRenderer VERDANA50 = this.getFont("VERDANA", 50.0F);
   public UnicodeFontRenderer arialBold10 = this.getFont("arialBold", 10.0F);
   public UnicodeFontRenderer arialBold11 = this.getFont("arialBold", 11.0F);
   public UnicodeFontRenderer arialBold12 = this.getFont("arialBold", 12.0F);
   public UnicodeFontRenderer arialBold13 = this.getFont("arialBold", 13.0F);
   public UnicodeFontRenderer arialBold15 = this.getFont("arialBold", 15.0F);
   public UnicodeFontRenderer arialBold16 = this.getFont("arialBold", 16.0F);
   public UnicodeFontRenderer arialBold17 = this.getFont("arialBold", 17.0F);
   public UnicodeFontRenderer arialBold18 = this.getFont("arialBold", 18.0F);
   public UnicodeFontRenderer arialBold20 = this.getFont("arialBold", 20.0F);
   public UnicodeFontRenderer arialBold25 = this.getFont("arialBold", 25.0F);
   public UnicodeFontRenderer arialBold30 = this.getFont("arialBold", 30.0F);
   public UnicodeFontRenderer arialBold35 = this.getFont("arialBold", 35.0F);
   public UnicodeFontRenderer arialBold40 = this.getFont("arialBold", 40.0F);
   public UnicodeFontRenderer arialBold45 = this.getFont("arialBold", 45.0F);
   public UnicodeFontRenderer arialBold50 = this.getFont("arialBold", 50.0F);
   public UnicodeFontRenderer robotobold10 = this.getFont("robotobold", 10.0F);
   public UnicodeFontRenderer robotobold11 = this.getFont("robotobold", 11.0F);
   public UnicodeFontRenderer robotobold12 = this.getFont("robotobold", 12.0F);
   public UnicodeFontRenderer robotobold13 = this.getFont("robotobold", 13.0F);
   public UnicodeFontRenderer robotobold15 = this.getFont("robotobold", 15.0F);
   public UnicodeFontRenderer robotobold16 = this.getFont("robotobold", 16.0F);
   public UnicodeFontRenderer robotobold17 = this.getFont("robotobold", 17.0F);
   public UnicodeFontRenderer robotobold18 = this.getFont("robotobold", 18.0F);
   public UnicodeFontRenderer robotobold20 = this.getFont("robotobold", 20.0F);
   public UnicodeFontRenderer robotobold25 = this.getFont("robotobold", 25.0F);
   public UnicodeFontRenderer robotobold30 = this.getFont("robotobold", 30.0F);
   public UnicodeFontRenderer robotobold35 = this.getFont("robotobold", 35.0F);
   public UnicodeFontRenderer robotobold40 = this.getFont("robotobold", 40.0F);
   public UnicodeFontRenderer robotobold45 = this.getFont("robotobold", 45.0F);
   public UnicodeFontRenderer robotobold50 = this.getFont("robotobold", 50.0F);

   public UnicodeFontRenderer getFont(String name, float size) {
      UnicodeFontRenderer unicodeFont = null;

      try {
         if(this.fonts.containsKey(name) && ((HashMap)this.fonts.get(name)).containsKey(Float.valueOf(size))) {
            return (UnicodeFontRenderer)((HashMap)this.fonts.get(name)).get(Float.valueOf(size));
         }

         InputStream e = this.getClass().getResourceAsStream("fonts/" + name + ".ttf");
         Font font = null;
         font = Font.createFont(0, e);
         unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
         unicodeFont.setUnicodeFlag(true);
         unicodeFont.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
         HashMap map = new HashMap();
         if(this.fonts.containsKey(name)) {
            map.putAll((Map)this.fonts.get(name));
         }

         map.put(Float.valueOf(size), unicodeFont);
         this.fonts.put(name, map);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return unicodeFont;
   }
   
   

   public UnicodeFontRenderer getFont(String name, float size, boolean b) {
      UnicodeFontRenderer unicodeFont = null;

      try {
         if(this.fonts.containsKey(name) && ((HashMap)this.fonts.get(name)).containsKey(Float.valueOf(size))) {
            return (UnicodeFontRenderer)((HashMap)this.fonts.get(name)).get(Float.valueOf(size));
         }

         InputStream e = this.getClass().getResourceAsStream("fonts/" + name + ".otf");
         Font font = null;
         font = Font.createFont(0, e);
         unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
         unicodeFont.setUnicodeFlag(true);
         unicodeFont.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
         HashMap map = new HashMap();
         if(this.fonts.containsKey(name)) {
            map.putAll((Map)this.fonts.get(name));
         }

         map.put(Float.valueOf(size), unicodeFont);
         this.fonts.put(name, map);
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      return unicodeFont;
   }
}
