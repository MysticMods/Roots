package epicsquid.roots.capability.spell;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.modules.ModuleRegistry;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellHolderCapability implements ISpellHolderCapability {

    private Map<Integer, SpellBase> spells = new HashMap<>();
    private Map<Integer, List<SpellModule>> spellModules = new HashMap<>();
    private int selectedSlot = 0;

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound compound = new NBTTagCompound();
        for(Map.Entry<Integer, SpellBase> entry : this.spells.entrySet()){
            compound.setString("spell_"+entry.getKey(), entry.getValue().getName());
        }

        for(Map.Entry<Integer, List<SpellModule>> entry : this.spellModules.entrySet()){
            List<SpellModule> modules = entry.getValue();
            for (int i = 0; i < modules.size(); i++){
                compound.setString(entry.getKey() + "_spell_" + i, modules.get(i).getName());
            }
        }

        compound.setInteger("selectedSlot", this.selectedSlot);
        return compound;
    }

    @Override
    public void setData(NBTTagCompound tag) {
        for(int i = 0; i < 5; i++){
            if(!tag.getString("spell_" + i).isEmpty()){
                spells.put(i, SpellRegistry.getSpell(tag.getString("spell_" + i)));
            }
        }

        for(int i = 0; i < 5; i++){
            List<SpellModule> modules = new ArrayList<>();
            for(int j = 0; j < 5; j++){
                if(!tag.getString(i +"_spell_" + j).isEmpty()){
                    modules.add(ModuleRegistry.getModule(tag.getString(i +"_spell_" + j)));
                }
            }
            if(modules.size() > 0){
                this.spellModules.put(i, modules);
            }
        }

        this.selectedSlot = tag.getInteger("selectedSlot");
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void clean() {

    }
}
