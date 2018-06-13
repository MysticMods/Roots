package teamroots.roots.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import teamroots.roots.entity.EntityFireJet;

public class SpellOrangeTulip extends SpellBase {

	public SpellOrangeTulip(String name) {
		super(name,TextFormatting.GOLD,255f/255f,128f/255f,32f/255f,255f/255f,64f/255f,32f/255f);
		this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
		this.cooldown = 24;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			EntityFireJet fireJet = new EntityFireJet(player.world);
			fireJet.setPlayer(player.getUniqueID());
			fireJet.setPosition(player.posX, player.posY, player.posZ);
			player.world.spawnEntity(fireJet);
		}
	}

}
