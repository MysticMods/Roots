package epicsquid.roots.event;

import epicsquid.roots.Roots;
import epicsquid.roots.util.XPUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class AdvancementHandler {
  public static int FIRST_CRAFT_XP = 4;

  private static void grantXP(ResourceLocation adv, World world, BlockPos pos) {
    if (adv.getNamespace().equals(Roots.MODID)) {
      switch (adv.getPath()) {
        case "baffle_cap":
        case "cloud_berry":
        case "dewgonia":
        case "infernal_bulb":
        case "moonglow_leaf":
        case "pereskia":
        case "spirit_herbs":
        case "stalicripe":
        case "terra_moss":
          XPUtil.spawnXP(world, pos, FIRST_CRAFT_XP);
      }
    }
  }

  public static void onAdvancement(AdvancementEvent event) {
    if (event.getEntityPlayer().world.isRemote) return;

    ResourceLocation adv = event.getAdvancement().getId();
    if (adv.getNamespace().equals(Roots.MODID)) {
      if (adv.getPath().equals("pacifist")) {
        return;
      }
      World world = event.getEntityPlayer().world;
      BlockPos pos = event.getEntityPlayer().getPosition();
      grantXP(adv, world, pos);

      Advancement advancement = event.getAdvancement();
      EntityPlayer player = event.getEntityPlayer();
      if (player.world.isRemote) return;

      PlayerAdvancements advancements = ((EntityPlayerMP) player).getAdvancements();
      for (Advancement parent = advancement.getParent(); parent != null; parent = parent.getParent()) {
        AdvancementProgress progress = advancements.getProgress(parent);
        if (progress.isDone()) continue;
        for (String criterion : progress.getRemaningCriteria()) {
          progress.grantCriterion(criterion);
        }
        advancement.getRewards().apply((EntityPlayerMP) player);
        grantXP(parent.getId(), world, pos);
      }
      advancements.save();
    }
  }
}
