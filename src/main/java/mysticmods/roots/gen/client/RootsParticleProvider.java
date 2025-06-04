package mysticmods.roots.gen.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModParticles;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

import java.util.ArrayList;
import java.util.List;

public class RootsParticleProvider extends ParticleDescriptionProvider {
  public RootsParticleProvider(PackOutput output, ExistingFileHelper fileHelper) {
    super(output, fileHelper);
  }

  @Override
  protected void addDescriptions() {
    this.sprite(ModParticles.PYRE.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.SYLVAN_LIGHT.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.METEOR.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.WILDFIRE.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.GROWTH.get(), RootsAPI.rl("simple_square"));
    this.sprite(ModParticles.CHANNEL_TARGET.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.CHANNEL.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.CHANNEL_FAIL.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.CHANNEL_JAUNT.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.AIR_BUBBLE.get(), ResourceLocation.withDefaultNamespace("bubble"));

    this.sprite(ModParticles.ANIMAL_HARVEST.get(), RootsAPI.rl("simple"));

    this.sprite(ModParticles.TEST.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.WIND.get(), RootsAPI.rl("wind"));
    this.sprite(ModParticles.DANDELION.get(), RootsAPI.rl("dandelion"));

    this.spriteSet(ModParticles.SKY_SOARER.get(), RootsAPI.rl("feather"), 2, false);
    this.sprite(ModParticles.SKY_SOARER_PUFF.get(), RootsAPI.rl("simple"));

    this.sprite(ModParticles.EXTENSION.get(), RootsAPI.rl("simple"));

    this.sprite(ModParticles.SHATTER_BEAM.get(), RootsAPI.rl("simple"));

    this.sprite(ModParticles.MAGNETISM.get(), RootsAPI.rl("simple"));

    this.sprite(ModParticles.LIFE_DRAINED.get(), RootsAPI.rl("simple"));

    this.sprite(ModParticles.LIFE_DRAIN.get(), RootsAPI.rl("simple"));

    this.sprite(ModParticles.PETAL_SHELL.get(), RootsAPI.rl("petal_1"));

    //this.sprite(ModParticles.SMOKE.get(), RootsAPI.rl("smoke"));
    this.spriteSet(ModParticles.SMOKE.get(), ResourceLocation.withDefaultNamespace("generic"), 8, true);

    this.spriteSet(ModParticles.DISARM.get(), RootsAPI.rl("petal"), 4, false);
    this.spriteSet(ModParticles.PETAL.get(), RootsAPI.rl("petal"), 4, false);

    this.spriteSet(ModParticles.SPIRAL.get(), RootsAPI.rl("petal"), 4, false);

    this.spriteSet(ModParticles.PYRE_LEAF.get(), RootsAPI.rl("leaf"), 5, false);
    List<ResourceLocation> leafPetals = List.of(RootsAPI.rl("leaf_0"), RootsAPI.rl("leaf_1"), RootsAPI.rl("leaf_2"), RootsAPI.rl("leaf_3"), RootsAPI.rl("leaf_4"), RootsAPI.rl("petal_0"), RootsAPI.rl("petal_1"), RootsAPI.rl("petal_2"), RootsAPI.rl("petal_3"));
    this.spriteSet(ModParticles.GROVE_STONE.get(), leafPetals);
    this.spriteSet(ModParticles.WHIRLWIND.get(), leafPetals);

    this.spriteSet(ModParticles.HARVEST.get(), ResourceLocation.withDefaultNamespace("sweep"), 8, false);

    this.sprite(ModParticles.SPROUT_PORTAL.get(), RootsAPI.rl("simple"));

    this.spriteSet(ModParticles.FOOD.get(), RootsAPI.rl("food"), 2, false);

    this.spriteSet(ModParticles.HEAL.get(), ResourceLocation.withDefaultNamespace("heart"));

    this.sprite(ModParticles.FOG.get(), RootsAPI.rl("smoke"));

    this.sprite(ModParticles.SANCTUARY.get(), RootsAPI.rl("simple"));

    List<ResourceLocation> sga = new ArrayList<>();
    for (char c : "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
      sga.add(ResourceLocation.withDefaultNamespace("sga_" + c));
    }

    this.spriteSet(ModParticles.NONDETECTION.get(), sga);
  }

  @Override
  public String getName() {
    return "Roots Particle Provider";
  }
}