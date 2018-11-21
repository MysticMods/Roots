package epicsquid.roots.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class EffectTimeStop extends Effect {
  public EffectTimeStop(String name, boolean hasIcon) {
    super(name, hasIcon);
  }

  @Override
  public void onTick(EntityLivingBase entity, int remainingDuration, NBTTagCompound tag){
    super.onTick(entity, remainingDuration, tag);
    entity.hurtResistantTime = 0;
    entity.hurtTime = 0;
  }
}