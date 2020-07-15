package epicsquid.roots.potion;

import epicsquid.roots.Roots;
import epicsquid.roots.spell.SpellAquaBubble;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("NullableProblems")
public class PotionAquaBubble extends Potion {
  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/potions.png");
  private float absorb_amount = 0;

  public PotionAquaBubble() {
    super(false, 0x3ea1ed);
    setPotionName("Aqua Bubble");
    setBeneficial();
    setIconIndex(8, 0);
  }

  public void finalise(SpellAquaBubble spell) {
    absorb_amount = (float) spell.absorption;
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }

  @Override
  public boolean shouldRender(PotionEffect effect) {
    return true;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public int getStatusIconIndex() {
    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    return super.getStatusIconIndex();
  }

  @Override
  public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
    entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() - absorb_amount);
    super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
  }

  @Override
  public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
    entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() + absorb_amount);
    super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
  }
}
