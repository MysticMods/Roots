package epicsquid.roots.client;

import epicsquid.roots.client.gui.GuiFakeSpectator;

public class SpectatorHandler {
	public static void setFake() {
		GuiFakeSpectator.setFake();
	}
	
	public static void setReal() {
		GuiFakeSpectator.setReal();
	}
}
