package epicsquid.roots;

import epicsquid.roots.integration.crafttweaker.*;
import epicsquid.roots.integration.crafttweaker.tweaks.*;
import epicsquid.roots.integration.crafttweaker.tweaks.predicates.*;
import epicsquid.roots.util.zen.ZenDocExporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExportDocumentation {

  public static void main(String[] args) {
    String targetPath = "../roots/docs/zs/";
    Class<?>[] classes = {
        AnimalHarvestTweaker.class,
        BarkTweaker.class,
        FeyCraftingTweaker.class,
        FlowerTweaker.class,
        MortarTweaker.class,
        PacifistTweaker.class,
        PyreCraftingTweaker.class,
        RunicShearsTweaker.class,
        Rituals.class,
        ChrysopoeiaTweaker.class,
        TransmutationTweaker.class,
        BlockStateAbove.class,
        BlockStateBelow.class,
        Predicates.class,
        PropertyPredicate.class,
        StatePredicate.class,
        Herbs.class,
        Spells.class,
        Spell.class,
        Ritual.class
    };

    ZenDocExporter export = new ZenDocExporter();
    Path path = Paths.get(targetPath);

    try {
      Files.createDirectories(path);
      export.export(path, classes);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
