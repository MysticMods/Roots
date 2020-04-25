package epicsquid.roots.spell.info;

import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class StaffSpellInfo extends AbstractSpellModifiers<ModifierInstanceList> {
  private ModifierInstanceList modifiers = new ModifierInstanceList();
  private long cooldownStart = -1;

  private StaffSpellInfo() {
  }

  private StaffSpellInfo(SpellBase spell) {
    super(spell);
  }

  @Nullable
  @Override
  public ModifierInstanceList getModifiers() {
    return modifiers;
  }

  public boolean onCooldown() {
    return cooldown() > 0;
  }

  public int cooldownLeft() {
    if (cooldownStart == -1) {
      return -1;
    }

    int internal = (int) ((System.currentTimeMillis() - this.cooldownStart) / 1000);

    if (internal > spell.getCooldown() || internal < 0) {
      this.cooldownStart = -1;
      return -1;
    } else {
      return internal;
    }
  }

  public int cooldown () {
    return spell.getCooldown();
  }

  public void use() {
    this.cooldownStart = System.currentTimeMillis();
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound result = super.serializeNBT();
    result.setTag("m", modifiers.serializeNBT());
    result.setLong("l", cooldownStart);
    return result;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    super.deserializeNBT(nbt);
    this.modifiers = ModifierInstanceList.fromNBT(nbt.getTagList("m", Constants.NBT.TAG_COMPOUND));
    this.cooldownStart = nbt.getLong("l");
  }

  public static StaffSpellInfo fromNBT(NBTTagCompound tag) {
    StaffSpellInfo instance = new StaffSpellInfo();
    instance.deserializeNBT(tag);
    return instance;
  }

  @Nullable
  public static StaffSpellInfo fromRegistry (String name) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    SpellBase spell = SpellRegistry.getSpell(rl);
    if (spell == null) {
      return null;
    }
    return new StaffSpellInfo(spell);
  }
}
