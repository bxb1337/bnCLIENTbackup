package net.AzureWare.ui.particles;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.util.vector.Vector2f;

import net.AzureWare.ui.particles.particle.Particle;
import net.AzureWare.ui.particles.particle.ParticleSnow;

public class ParticleManager {
	private Particle particle;
    private int amount;
    public ArrayList<Particle> particles = new ArrayList();
    private Random random = new Random();

    public ParticleManager(Particle particle, int amount) {
        this.particle = particle;
        this.amount = amount;
        this.init();
    }

    private void init() {
        this.particles.clear();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int i = 0;
        while (i < this.amount) {
            ParticleSnow particle = new ParticleSnow();
            if (particle instanceof ParticleSnow) {
                particle = new ParticleSnow();
            }
            particle.vector.x = this.random.nextInt(res.getScaledWidth() + 1);
            particle.vector.y = this.random.nextInt(res.getScaledHeight() + 1);
            this.particles.add(particle);
            ++i;
        }
    }

    public void draw(int xAdd) {
        for (Particle particle : this.particles) {
            particle.draw(xAdd);
        }
    }
}
