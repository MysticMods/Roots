package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class ModifierPacket implements IMessage {
  private StaffModifierInstanceList modifierInstances;
  @SideOnly(Side.CLIENT)
  protected ModifierSnapshot modifiers = null;

  public ModifierPacket () {
    this.modifiers = null;
    this.modifierInstances = null;
  }

  public ModifierPacket(ModifierSnapshot snapshot) {
    this.modifiers = snapshot;
  }

  public ModifierPacket(StaffModifierInstanceList modifierInstances) {
    this.modifierInstances = modifierInstances;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.modifiers = StaffModifierInstanceList.fromBytes(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    if (this.modifiers != null) {
      this.modifiers.toBytes(buf);
    } else {
      if (this.modifierInstances == null) {
        buf.writeInt(0);
      } else {
        this.modifierInstances.toBytes(buf);
      }
    }
  }

  public boolean has(IModifier modifier) {
    return modifiers != null && modifiers.has(modifier);
  }

  public boolean hasRand (IModifier modifier, int rand) {
    return has(modifier) && Util.rand.nextInt(rand) == 0;
  }
}
