package mysticmods.roots.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class WitchHeadModel extends VillagerHeadModel {

  public WitchHeadModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createWitchHead() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    PartDefinition partdefinition1 = partdefinition.addOrReplaceChild(
        "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO
    );
    PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild(
        "hat", CubeListBuilder.create().texOffs(0, 64)
            .addBox(0.0F, 0.0F, 0.0F, 10.0F, 2.0F, 10.0F), PartPose.offset(-5.0F, -10.03125F, -5.0F)
    );
    PartDefinition partdefinition3 = partdefinition2.addOrReplaceChild(
        "hat2",
        CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 7.0F, 4.0F, 7.0F),
        PartPose.offsetAndRotation(1.75F, -4.0F, 2.0F, -0.05235988F, 0.0F, 0.02617994F)
    );
    PartDefinition partdefinition4 = partdefinition3.addOrReplaceChild(
        "hat3",
        CubeListBuilder.create().texOffs(0, 87).addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F),
        PartPose.offsetAndRotation(1.75F, -4.0F, 2.0F, -0.10471976F, 0.0F, 0.05235988F)
    );
    partdefinition4.addOrReplaceChild(
        "hat4",
        CubeListBuilder.create().texOffs(0, 95).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.25F)),
        PartPose.offsetAndRotation(1.75F, -2.0F, 2.0F, (float) (-Math.PI / 15), 0.0F, 0.10471976F)
    );
    partdefinition1.addOrReplaceChild(
        "nose", CubeListBuilder.create().texOffs(24, 0)
            .addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -2.0F, 0.0F)
    );
    PartDefinition partdefinition5 = partdefinition1.getChild("nose");
    partdefinition5.addOrReplaceChild(
        "mole",
        CubeListBuilder.create().texOffs(0, 0)
            .addBox(0.0F, 3.0F, -6.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
        PartPose.offset(0.0F, -2.0F, 0.0F)
    );
    return LayerDefinition.create(meshdefinition, 64, 128);
  }
}
