package epicsquid.roots.recipe;

import epicsquid.roots.Roots;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ServerWorld;

import java.util.List;

public class SalmonRecipe extends FeyCraftingRecipe {
  public SalmonRecipe(ItemStack result, int xp) {
    super(result, xp);
  }

  @Override
  public void postCraft(ItemStack output, List<ItemStack> inputs, PlayerEntity crafter) {
    if (crafter == null || crafter.world.isRemote) {
      return;
    }

    ListNBT tagList = new ListNBT();
    ServerPlayerEntity player = (ServerPlayerEntity) crafter;
    ServerWorld world = (ServerWorld) player.world;
    AdvancementManager manager = world.getAdvancementManager();
    PlayerAdvancements advancements = player.getAdvancements();
    for (Advancement advancement : manager.getAdvancements()) {
      ResourceLocation id = advancement.getId();
      if (id.getNamespace().equals(Roots.MODID) && !id.getPath().equals("pacifist")) {
        AdvancementProgress progress = advancements.getProgress(advancement);
        if (progress.isDone()) {
          tagList.add(new StringNBT(advancement.getId().getPath()));
        }
      }
    }

    CompoundNBT tag = output.getTagCompound();
    if (tag == null) {
      tag = new CompoundNBT();
      output.setTagCompound(tag);
    }

    tag.put("advancements", tagList);
    tag.setString("crafter", crafter.getDisplayNameString());
  }
}
