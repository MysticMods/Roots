#!/usr/bin/env python

import os, sys, re

MODIFIER_FINDER = re.compile("public static Modifier [^ ]+ = ModifierRegistry.register\(new Modifier\(new ResourceLocation\(Roots.MODID, \"([^\"]+)\"\),")
SPELL_FINDER = re.compile("public static ResourceLocation spellName = new ResourceLocation\(Roots.MODID, \"([^\"]+)\"")
TEMPLATE = """    {
      "type": "spell_modifier_template",
      "spell": "%s",
      "modifier": "roots:%s"
    }"""

def main (args):
    for f in os.listdir("src/main/java/epicsquid/roots/spell"):
        if os.path.isdir(f):
            continue

        if not f.endswith(".java") or not f.startswith("Spell"):
            continue

        if f == "SpellBase.java" or f == "SpellRegistry.java":
            continue

        with open(os.path.join("src/main/java/epicsquid/roots/spell", f)) as o:
            data = o.read()

        spell_name = SPELL_FINDER.findall(data)[0]

        values = []
        for mod in MODIFIER_FINDER.findall(data):
            values.append(TEMPLATE % (spell_name, mod))

        with open("spells/" + spell_name + ".json", "w") as o:
            o.write(",\n".join(values))

if __name__=="__main__":
    main(sys.argv)