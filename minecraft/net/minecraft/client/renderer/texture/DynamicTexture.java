package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class DynamicTexture extends AbstractTexture
{
    private int[] dynamicTextureData;

    /** width of this icon in pixels */
    private int width;

    /** height of this icon in pixels */
    private int height;
    private static final String __OBFID = "CL_00001048";
    private boolean shadersInitialized;

    public DynamicTexture(BufferedImage bufferedImage)
    {
    	try {
            this.shadersInitialized = false;
            this.width = bufferedImage.getWidth();
            this.height = bufferedImage.getHeight();
            this.dynamicTextureData = new int[bufferedImage.getWidth() * bufferedImage.getHeight() * 3];

            if (Config.isShaders())
            {
                ShadersTex.initDynamicTexture(this.getGlTextureId(), bufferedImage.getWidth(), bufferedImage.getHeight(), this);
                this.shadersInitialized = true;
            }
            else
            {
                TextureUtil.allocateTexture(this.getGlTextureId(), bufferedImage.getWidth(), bufferedImage.getHeight());
            }
            
            bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.dynamicTextureData, 0, bufferedImage.getWidth());
            this.updateDynamicTexture();
    	}catch (Throwable e) {
    		
    	}
    }

    public DynamicTexture(int textureWidth, int textureHeight)
    {
    	try {
            this.shadersInitialized = false;
            this.width = textureWidth;
            this.height = textureHeight;
            this.dynamicTextureData = new int[textureWidth * textureHeight * 3];

            if (Config.isShaders())
            {
                ShadersTex.initDynamicTexture(this.getGlTextureId(), textureWidth, textureHeight, this);
                this.shadersInitialized = true;
            }
            else
            {
                TextureUtil.allocateTexture(this.getGlTextureId(), textureWidth, textureHeight);
            }
    	}catch (Throwable e) {
    		
    	}
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
    }

    public void updateDynamicTexture()
    {
        if (Config.isShaders())
        {
            if (!this.shadersInitialized)
            {
                ShadersTex.initDynamicTexture(this.getGlTextureId(), this.width, this.height, this);
                this.shadersInitialized = true;
            }

            ShadersTex.updateDynamicTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height, this);
        }
        else
        {
            TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
        }
    }

    public int[] getTextureData()
    {
        return this.dynamicTextureData;
    }
}
