package net.AzureWare.utils.RDBProject;

public class RotationData {
	public float[] yaws;
	public int count = 0;
	public float curYaw;
	public boolean isUseless = false;
	
	public RotationData(float[] yawData) {
		yaws = yawData;
	}
	
	public float getNextYaw() {
		if (yaws.length <= count) isUseless = true;
		if (!isUseless) {
			curYaw = yaws[count];
		}else {
			curYaw = 0;
		}
		count++;
		return curYaw;
	}
	
	public void reset() {
		count = 0;
		isUseless = false;
	}
	
	public float getYawOffset() {
		float f = 0;
		for (float yaw : yaws) {
			f += yaw;
		}
		return f;
	}
	
	public int getSize() {
		return yaws.length;
	}
}
