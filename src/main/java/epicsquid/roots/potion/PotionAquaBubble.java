package epicsquid.roots.potion;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageAquaBubbleFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellAquaBubble;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("NullableProblems")
public class PotionAquaBubble extends Potion {
  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/thaumcraft.potions.png");
  private float absorb_amount = 0;
  private float amplifier = 0;

  public PotionAquaBubble() {
    super(false, 0x3ea1ed);
    setPotionName("Aqua Bubble");
    setBeneficial();
    setIconIndex(8, 0);
  }

  public void finalise(SpellAquaBubble spell) {
    absorb_amount = (float) spell.absorption;
    amplifier = spell.amplifier;
  }

  public static void doEffect(World world, double posX, double posY, double posZ) {
    for (float i = 0; i < 360; i += Util.rand.nextInt(40)) {
      float x = (float) posX + (1.2f * Util.rand.nextFloat()) * (float) Math.sin(Math.toRadians(i));
      float y = (float) posY + (Util.rand.nextFloat() + 0.25f);
      float z = (float) posZ + (1.2f * Util.rand.nextFloat()) * (float) Math.cos(Math.toRadians(i));
      float vx = 0.0625f * (float) Math.cos(Math.toRadians(i));
      float vz = 0.025f * (float) Math.sin(Math.toRadians(i));
      if (Util.rand.nextBoolean()) {
        vx *= -1;
        vz *= -1;
      }
      // TODO: Adjust smoke
      if (Util.rand.nextBoolean()) {
        ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, SpellAquaBubble.instance.getFirstColours(0.125f), 4f + Util.rand.nextFloat() * 6f, 120, false);
      } else {
        ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, SpellAquaBubble.instance.getSecondColours(0.125f), 4f + Util.rand.nextFloat() * 6f, 120, false);
      }
    }
  }

  @Override
  public void performEffect(EntityLivingBase entity, int amplifier) {
    if (!entity.world.isRemote) {
      ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(entity.getEntityData(), SpellAquaBubble.instance);
      if (mods.has(SpellAquaBubble.POISON_RESIST)) {
        entity.removePotionEffect(MobEffects.POISON);
      }
      if (!(entity instanceof EntityPlayer)) {
        if (Util.rand.nextInt(10) == 0) {
          PacketHandler.sendToAllTracking(new MessageAquaBubbleFX(entity.posX, entity.posY, entity.posZ), entity);
        }
      }
    } else {
      if (Util.rand.nextInt(10) == 0) {
        doEffect(entity.world, entity.posX, entity.posY, entity.posZ); // This is only the player
      }
    }
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
    ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(entityLivingBaseIn.getEntityData(), SpellAquaBubble.instance);
    float absorb = absorb_amount;
    if (mods.has(SpellAquaBubble.AMPLIFIED)) {
      absorb += absorb_amount * this.amplifier;
    }
    entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() - absorb);
    super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
  }

  @Override
  public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
    ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(entityLivingBaseIn.getEntityData(), SpellAquaBubble.instance);
    float absorb = absorb_amount;
    if (mods.has(SpellAquaBubble.AMPLIFIED)) {
      absorb += absorb_amount * this.amplifier;
    }
    entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() + absorb);
    super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
  }
}
