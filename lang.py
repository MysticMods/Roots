#!/usr/bin/env python

with open("roots.log") as o:
    prefixes = [x.strip()+"=" for x in o.readlines()]

with open("src/main/resources/assets/roots/lang/en_us.lang") as o:
    data = [x.strip() for x in o.readlines() if x.startswith("roots.modifiers")]

for prefix in prefixes:
    found = False
    for line in data:
        if line.startswith(prefix):
            found = True

    if not found:
        print("Modifier description not found: " + prefix)

for line in data:
    prefix = line.split("=")[0]+"="
    if prefix not in prefixes:
        print("Unused modifier description found: " + line)