package epicsquid.roots.init;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.entity.RenderNull;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.fairy.FairyEntity;
import epicsquid.roots.entity.projectile.FlareEntity;
import epicsquid.roots.entity.fairy.render.FairyRenderer;
import epicsquid.roots.entity.ritual.*;
import epicsquid.roots.entity.spell.BoostEntity;
import epicsquid.roots.entity.spell.FireJetEntity;
import epicsquid.roots.entity.spell.ThornTrapEntity;
import epicsquid.roots.entity.spell.TimeStopEntity;
import epicsquid.roots.entity.wild.WhiteStagEntity;
import epicsquid.roots.entity.wild.render.WhiteStagRenderer;
import epicsquid.roots.proxy.ClientProxy;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTables;

import java.util.Arrays;
import java.util.List;

public class ModEntities {

  /**
   * Registers mobs in the game
   * <p>
   * Egg colours are defined as Background colour then Foreground (spots) colour
   * <p>
   * <p>
   * Format for registering a mob:
   * <p>
   * LibRegistry.registerEntity(Entity.class, BackgroundColour, ForegroundColour);
   * if (Mod.proxy instanceof ClientProxy)
   * LibRegistry.registerEntityRenderer(Entity.class, new RenderEntity.Factory());
   */
  public static void registerMobs() {
    // Helper entities
    LibRegistry.registerEntity(FireJetEntity.class);
    LibRegistry.registerEntity(ThornTrapEntity.class);
    LibRegistry.registerEntity(TimeStopEntity.class);
    LibRegistry.registerEntity(BoostEntity.class);
    LibRegistry.registerEntity(FlareEntity.class);

    // Actual entities
    LibRegistry.registerEntity(FairyEntity.class, 0xf542e3, 0xdb7fa1);
    LibRegistry.registerEntity(WhiteStagEntity.class, 0xe0caba, 0x473124);

    // Ritual entities
    List<Class<? extends Entity>> ritualClasses = Arrays.asList(
        AnimalHarvestRitualEntity.class,
        DivineProtectionRitualEntity.class,
        FireStormRitualEntity.class,
        FlowerGrowthRitualEntity.class,
        FrostLandsRitualEntity.class,
        GatheringRitualEntity.class,
        GerminationRitualEntity.class,
        HealingAuraRitualEntity.class,
        HeavyStormsRitualEntity.class,
        OvergrowthRitualEntity.class,
        PurityRitualEntity.class,
        SpreadingForestRitualEntity.class,
        TransmutationRitualEntity.class,
        WardingProtectionRitualEntity.class,
        WildGrowthRitualEntity.class,
        WindwallRitualEntity.class
    );

    ritualClasses.forEach(LibRegistry::registerEntity);

    if (Roots.proxy instanceof ClientProxy) {
      LibRegistry.registerEntityRenderer(FireJetEntity.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(ThornTrapEntity.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(TimeStopEntity.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(BoostEntity.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(FlareEntity.class, new RenderNull.Factory());

      LibRegistry.registerEntityRenderer(FairyEntity.class, new FairyRenderer.Factory());
      LibRegistry.registerEntityRenderer(WhiteStagEntity.class, new WhiteStagRenderer.Factory());

      ritualClasses.forEach(c -> LibRegistry.registerEntityRenderer(c, new RenderNull.Factory()));
    }
  }

  public static List<ResourceLocation> LOOT_TABLES = Arrays.asList(FairyEntity.LOOT_TABLE, WhiteStagEntity.LOOT_TABLE);

  public static void registerLootTables() {
    LOOT_TABLES.forEach(LootTables::register);
  }
}
