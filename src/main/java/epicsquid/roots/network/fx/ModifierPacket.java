package epicsquid.roots.network.fx;

import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class ModifierPacket implements IMessage {
  private StaffModifierInstanceList modifierInstances;
  @SideOnly(Side.CLIENT)
  protected Set<IModifierCore> modifiers = null;

  public ModifierPacket(StaffModifierInstanceList modifierInstances) {
    this.modifierInstances = modifierInstances;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.modifiers = StaffModifierInstanceList.fromBytes(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    if (this.modifierInstances == null) {
      buf.writeInt(0);
    } else {
      this.modifierInstances.toBytes(buf);
    }
  }

  @SideOnly(Side.CLIENT)
  public boolean has(IModifier modifier) {
    return modifiers != null && modifiers.contains(modifier.getCore());
  }
}
