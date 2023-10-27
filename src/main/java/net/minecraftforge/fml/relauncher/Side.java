package net.minecraftforge.fml.relauncher;

public enum Side {
	CLIENT,
	SERVER;
	
	private Side() {
	}
	
	public boolean isServer() {
		return !this.isClient();
	}
	
	public boolean isClient() {
		return this == CLIENT;
	}
}
