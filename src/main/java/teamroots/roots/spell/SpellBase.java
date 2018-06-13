package teamroots.roots.spell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import teamroots.roots.util.InventoryUtil;

public class SpellBase {
	public float red1, green1, blue1;
	public float red2, green2, blue2;
	public TextFormatting textColor;
	public String name;
	public EnumCastType castType = EnumCastType.INSTANTANEOUS;
	public int cooldown = 20;
	public Map<String, Double> costs = new HashMap<String, Double>();
	
	public static enum EnumCastType {
		INSTANTANEOUS, CONTINUOUS
	}
	
	public boolean costsMet(EntityPlayer player){
		boolean matches = true;
		for (int i = 0; i < costs.size(); i ++){
			String s = costs.keySet().toArray(new String[costs.size()])[i];
			double d = costs.values().toArray(new Double[costs.size()])[i];
			matches = matches && InventoryUtil.getPowderTotal(player, s) >= d;
		}
		return matches && costs.size() > 0 || player.capabilities.isCreativeMode;
	}
	
	public void enactCosts(EntityPlayer player){
		for (int i = 0; i < costs.size(); i ++){
			String s = costs.keySet().toArray(new String[costs.size()])[i];
			double d = costs.values().toArray(new Double[costs.size()])[i];
			InventoryUtil.removePowder(player, s, d);
		}
	}
	
	public void enactTickCosts(EntityPlayer player){
		for (int i = 0; i < costs.size(); i ++){
			String s = costs.keySet().toArray(new String[costs.size()])[i];
			double d = costs.values().toArray(new Double[costs.size()])[i];
			InventoryUtil.removePowder(player, s, d/20.0);
		}
	}
	
	public void addToolTip(List<String> tooltip){
		tooltip.add(""+textColor+TextFormatting.BOLD+I18n.format("roots.spell."+name+".name")+TextFormatting.RESET);
		for (int i = 0; i < costs.size(); i ++){
			String s = costs.keySet().toArray(new String[costs.size()])[i];
			double d = costs.values().toArray(new Double[costs.size()])[i];
			tooltip.add(I18n.format(s+".name")+I18n.format("roots.tooltip.pouch_divider")+d);
		}
	}
	
	public SpellBase(String name, TextFormatting textColor, float r1, float g1, float b1, float r2, float g2, float b2){
		this.name = name;
		this.red1 = r1;
		this.green1 = g1;
		this.blue1 = b1;
		this.red2 = r2;
		this.green2 = g2;
		this.blue2 = b2;
		this.textColor = textColor;
	}
	
	public SpellBase addCost(Item herb, double amount){
		costs.put(herb.getUnlocalizedName(), amount);
		return this;
	}
	
	public void cast(EntityPlayer caster){
		//
	}
}
