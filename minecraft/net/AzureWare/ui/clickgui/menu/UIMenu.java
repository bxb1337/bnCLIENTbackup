package net.AzureWare.ui.clickgui.menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;

import net.AzureWare.Client;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class UIMenu {
	private ArrayList<UIMenuCategory> categories;
    public static int WIDTH = 100;
    public static int TAB_HEIGHT = 30;
    private MouseInputHandler handler = new MouseInputHandler(0);
    private Minecraft mc = Minecraft.getMinecraft();
    private String fileDir;
    
    public UIMenu() {
        this.fileDir = String.valueOf((Object)this.mc.mcDataDir.getAbsolutePath()) + "/" + Client.CLIENT_NAME;
        this.categories = new ArrayList();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        this.addCategorys();
        try {
            this.loadClickGui();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(int mouseX, int mouseY) {
        for (UIMenuCategory c : this.categories) {
            c.draw(mouseX, mouseY);
        }
    }

    private void addCategorys() {
        int xAxis = 10;
        Mod.Category[] arrcategory = Mod.Category.values();
        int n = arrcategory.length;
        int n2 = 0;
        while (n2 < n) {
            Mod.Category c = arrcategory[n2];
            if (c != Mod.Category.NONE) {
                this.categories.add(new UIMenuCategory(c, xAxis, 100, WIDTH, TAB_HEIGHT, this.handler));
                xAxis += 115;
            }
            ++n2;
        }
    }

    public void mouseClick(int mouseX, int mouseY) {
        for (UIMenuCategory cat : this.categories) {
            cat.mouseClick(mouseX, mouseY);
        }
    }

    public void mouseRelease(int mouseX, int mouseY) {
        for (UIMenuCategory cat : this.categories) {
            cat.mouseRelease(mouseX, mouseY);
        }
        Client.getInstance().getFileUtil().saveValues();
        this.saveClickGui();
    }

    public ArrayList<UIMenuCategory> getCategories() {
        return this.categories;
    }

    public void saveClickGui() {
        File f = new File(String.valueOf((Object)this.fileDir) + "/gui.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (UIMenuCategory cat : this.getCategories()) {
                String name = cat.c.name();
                String x = String.valueOf((int)cat.x);
                String y = String.valueOf((int)cat.y);
                String open = String.valueOf((boolean)cat.uiMenuMods.open);
                pw.print(String.valueOf((Object)name) + ":" + x + ":" + y + ":" + open + "\n");
            }
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("resource")
	public void loadClickGui() throws IOException {
        File f = new File(String.valueOf((Object)this.fileDir) + "/gui.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            BufferedReader br = new BufferedReader((Reader)new FileReader(f));
            while ((line = br.readLine()) != null) {
                try {
                    String[] s = line.split(":");
                    if (s.length < 4) continue;
                    String name = s[0];
                    int x = Integer.valueOf((String)s[1]);
                    int y = Integer.valueOf((String)s[2]);
                    boolean open = Boolean.valueOf((String)s[3]);
                    for (UIMenuCategory cat : this.getCategories()) {
                        String name2 = cat.c.name();
                        if (!name2.equals((Object)name)) continue;
                        cat.x = x;
                        cat.y = y;
                        cat.uiMenuMods.open = open;
                    }
                } catch (Exception s) {

                }
            }
        }
    }
}