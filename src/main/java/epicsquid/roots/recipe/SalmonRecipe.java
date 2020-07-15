package epicsquid.roots.recipe;

import epicsquid.roots.Roots;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

import java.util.List;

public class SalmonRecipe extends FeyCraftingRecipe {
  public SalmonRecipe(ItemStack result, int xp) {
    super(result, xp);
  }

  @Override
  public void postCraft(ItemStack output, List<ItemStack> inputs, EntityPlayer crafter) {
    if (crafter == null || crafter.world.isRemote) {
      return;
    }

    NBTTagList tagList = new NBTTagList();
    EntityPlayerMP player = (EntityPlayerMP) crafter;
    WorldServer world = (WorldServer) player.world;
    AdvancementManager manager = world.getAdvancementManager();
    PlayerAdvancements advancements = player.getAdvancements();
    for (Advancement advancement : manager.getAdvancements()) {
      ResourceLocation id = advancement.getId();
      if (id.getNamespace().equals(Roots.MODID) && !id.getPath().equals("pacifist")) {
        AdvancementProgress progress = advancements.getProgress(advancement);
        if (progress.isDone()) {
          tagList.appendTag(new NBTTagString(advancement.getId().getPath()));
        }
      }
    }

    NBTTagCompound tag = output.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      output.setTagCompound(tag);
    }

    tag.setTag("advancements", tagList);
    tag.setString("crafter", crafter.getDisplayNameString());
  }
}
