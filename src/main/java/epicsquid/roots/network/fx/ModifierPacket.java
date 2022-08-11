package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.instance.staff.ISnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ModifierPacket implements IMessage {
	protected ISnapshot modifiers;
	
	public ModifierPacket() {
		this.modifiers = null;
	}
	
	public ModifierPacket(ISnapshot snapshot) {
		this.modifiers = snapshot;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.modifiers = StaffModifierInstanceList.fromBytes(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		if (this.modifiers == null) {
			buf.writeInt(0);
		} else {
			this.modifiers.toBytes(buf);
		}
	}
	
	public boolean has(IModifier modifier) {
		return modifiers != null && modifiers.has(modifier);
	}
	
	public boolean hasRand(IModifier modifier, int rand) {
		return has(modifier) && Util.rand.nextInt(rand) == 0;
	}
}
