package com.moonjew.bos.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.moonjew.bos.BlowingOffSteam;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = BlowingOffSteam.WIDTH;
		config.height = BlowingOffSteam.HEIGHT;
		new LwjglApplication(new BlowingOffSteam(), config);
	}
}
