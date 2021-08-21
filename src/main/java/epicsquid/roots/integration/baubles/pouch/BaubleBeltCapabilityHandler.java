package epicsquid.roots.integration.baubles.pouch;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.BaubleItem;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BaubleBeltCapabilityHandler implements ICapabilityProvider {
  public static ResourceLocation IDENTIFIER = new ResourceLocation("roots", "baubles_pouch");

  public static final BaubleBeltCapabilityHandler instance = new BaubleBeltCapabilityHandler();

  public static IBauble bauble = new BaubleItem(BaubleType.BELT);

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE) {
      return BaublesCapabilities.CAPABILITY_ITEM_BAUBLE.cast(bauble);
    }

    return null;
  }
}
