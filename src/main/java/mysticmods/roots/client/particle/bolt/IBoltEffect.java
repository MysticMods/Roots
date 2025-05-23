package mysticmods.roots.client.particle.bolt;

import mysticmods.roots.client.particle.Color;

import java.util.List;

public interface IBoltEffect {
  IBoltEffect count(int count);

  IBoltEffect size(float size);

  IBoltEffect spawn(SpawnFunction spawnFunction);

  IBoltEffect fade(FadeFunction fadeFunction);

  IBoltEffect lifespan(int lifespan);

  int getLifespan();

  SpawnFunction getSpawnFunction();

  FadeFunction getFadeFunction();

  Color getColor();

  List<BoltQuads> generate(float partialTicks);

  int getCount();
}
