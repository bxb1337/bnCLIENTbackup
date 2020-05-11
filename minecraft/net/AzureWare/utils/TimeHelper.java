package net.AzureWare.utils;

import net.AzureWare.Client;
import net.minecraft.client.main.Main;

public class TimeHelper {
	public long lastMs;
	private long prevMS;

	public TimeHelper() {
		this.lastMs = 0L;
	}

	public boolean isDelayComplete(final long delay) {
		if (!Client.INSTANCE)
			Runtime.getRuntime().exit(0);
		return System.currentTimeMillis() - this.lastMs > delay;
	}

	public boolean isDelayComplete(final double delay) {
		if (!Client.INSTANCE)
			Runtime.getRuntime().exit(0);
		return System.currentTimeMillis() - this.lastMs > delay;
	}

	public long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}

	public void reset() {
		if (!Client.INSTANCE)
			Runtime.getRuntime().exit(0);
		this.lastMs = System.currentTimeMillis();
	}

	public long getLastMs() {
		return this.lastMs;
	}

	public void setLastMs(final int i) {
		this.lastMs = System.currentTimeMillis() + i;
	}

	public boolean hasReached(final long milliseconds) {
		return System.currentTimeMillis() - this.lastMs >= milliseconds;
	}
	public boolean hasReached(final double milliseconds) {
		return System.currentTimeMillis() - this.lastMs >= milliseconds;
	}
	public boolean delay(float milliSec) {
		if (!Client.INSTANCE)
			Runtime.getRuntime().exit(0);
		return (float) (getTime() - this.prevMS) >= milliSec;
	}

	public long getTime() {
		return System.nanoTime() / 1000000L;
	}

	public long getDifference() {
		return getTime() - this.prevMS;
	}

	public boolean check(float milliseconds) {
		return getTime() >= milliseconds;
	}

	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

}
