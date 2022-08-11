package epicsquid.roots.modifiers.instance.staff;

import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.spell.ISpellMulitipliers;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.nbt.NBTTagCompound;

public class ModifierSnapshot implements ISnapshot, ISpellMulitipliers {
	private static IntArraySet EMPTY = new IntArraySet();
	
	private IntArraySet modifiers;
	
	public ModifierSnapshot() {
		this(EMPTY);
	}
	
	public ModifierSnapshot(IntArraySet modifiers) {
		this.modifiers = modifiers;
	}
	
	public ModifierSnapshot(int[] modifiers) {
		this.modifiers = new IntArraySet(modifiers);
	}
	
	@Override
	public int[] toArray() {
		return modifiers.toIntArray();
	}
	
	@Override
	public boolean has(IModifier modifier) {
		return this.modifiers.contains(modifier.getCore().getKey());
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(modifiers.size());
		for (int i : modifiers) {
			buf.writeInt(i);
		}
	}
	
	@Override
	public void toCompound(NBTTagCompound tag) {
		tag.setIntArray("modifiers", modifiers.toIntArray());
	}
}
