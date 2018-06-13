package teamroots.roots.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.util.Misc;

public class EffectWisdom extends Effect {
	public EffectWisdom(String name, boolean hasIcon) {
		super(name, hasIcon);
	}
}
