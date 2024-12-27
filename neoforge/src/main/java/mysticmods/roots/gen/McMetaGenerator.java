package mysticmods.roots.gen;

import com.google.gson.JsonObject;
import net.minecraft.DetectedVersion;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.server.packs.PackType;

import java.io.IOException;

public class McMetaGenerator implements DataProvider {
  private final DataGenerator generator;
  public McMetaGenerator(DataGenerator generator) {
    this.generator = generator;
  }

  @Override
  public void run(CachedOutput pOutput) throws IOException {
    JsonObject output = new JsonObject();
    JsonObject pack = new JsonObject();
    JsonObject desc = new JsonObject();
    desc.addProperty("text", "Resources for Roots 4");
    pack.add("description", desc);
    pack.addProperty("pack_format", DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES.bridgeType));
    output.add("pack", pack);
    DataProvider.saveStable(pOutput, output, this.generator.getOutputFolder().resolve("pack.mcmeta"));
  }

  @Override
  public String getName() {
    return "pack.mcmeta generator";
  }
}
