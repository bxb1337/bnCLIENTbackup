package net.AzureWare.mod.mods.RENDER;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import net.AzureWare.mod.Mod;
import net.minecraft.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;

public class XRay extends Mod {
    public static List<Integer> blocks = new ArrayList<Integer>();

    public XRay() {
        super("XRay", Category.RENDER);
        this.blocks.add(16);
        this.blocks.add(56);
        this.blocks.add(14);
        this.blocks.add(15);
        this.blocks.add(129);
        this.blocks.add(73);
    }
	@Override
    public void onEnable() {
    	Block.Xray = true;
        this.mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
    	Block.Xray = false;
        this.mc.renderGlobal.loadRenderers();
    }

    public static List<Integer> getBlocks() {
        return blocks;
    }
}