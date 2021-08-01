#!/usr/bin/env python

import os, sys, re

MODIFIER_FINDER = re.compile("public static Modifier [^ ]+ = ModifierRegistry.register\(new Modifier\(new ResourceLocation\(Roots.MODID, \"([^\"]+)\"\),")
SPELL_FINDER = re.compile("public static ResourceLocation spellName = new ResourceLocation\(Roots.MODID, \"([^\"]+)\"")
TEMPLATE = """    {
      "type": "spell_modifier_template",
      "spell": "%s",
      "modifier": "roots:%s",
      "anchor": "%s"
    },"""
EXISTING_MODIFIER = re.compile("""( +{\n +"type": "spell_modifier_template",\n +"spell": "[^"]+",\n +"modifier": "([^"]+)"\n +\},?)""", re.MULTILINE | re.I)

spell_data = {}

def main (args):
    for f in os.listdir("src/main/java/epicsquid/roots/spell"):
        full = os.path.join("src/main/java/epicsquid/roots/spell", f)
        if os.path.isdir(full):
            continue

        if not f.endswith(".java") or not f.startswith("Spell"):
            continue

        if f == "SpellBase.java" or f == "SpellRegistry.java":
            continue

        with open(os.path.join("src/main/java/epicsquid/roots/spell", f)) as o:
            data = o.read()

        spell_name = SPELL_FINDER.findall(data)[0]

        values = []
        #spell_data[spell_name] = {}
        for mod in MODIFIER_FINDER.findall(data):
            values.append(TEMPLATE % (spell_name, mod, mod))
            #spell_data[spell_name]["roots:" + mod] = TEMPLATE % (spell_name, mod, mod)

        with open(os.path.join("spells", spell_name + ".json"), "w") as o:
            o.write("\n".join(values))

    return

    for f in os.listdir("src/main/resources/assets/roots/patchouli_books/roots_guide/en_us/entries/spells"):
        full = os.path.join("src/main/resources/assets/roots/patchouli_books/roots_guide/en_us/entries/spells", f)

        if not f.startswith("spell_"):
            continue

        spell_name = f.replace(".json", "")
        with open(full) as o:
            data = o.read()

        for mod in EXISTING_MODIFIER.findall(data):
            data = data.replace(mod[0], spell_data[spell_name][mod[1]])

        with open(full, "w") as o:
            o.write(data)

if __name__=="__main__":
    main(sys.argv)