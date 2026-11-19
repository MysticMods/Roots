#!/usr/bin/env python

import sys
import json


def fmt(n):
    # Print 3 instead of 3.0, but keep real fractions like 2.5
    return str(int(n)) if float(n).is_integer() else str(n)


def parse(filename):
    with open(filename) as o:
        model = json.load(o)

    boxes = []
    for element in model.get("elements", []):
        coords = element["from"] + element["to"]
        boxes.append("Block.box(" + ", ".join(fmt(c) for c in coords) + ")")

    print("// " + filename)
    print("VoxelUtil.multiOr(" + ", ".join(boxes) + ");")


def main(args):
    for fn in args:
        if fn.endswith(".json"):
            parse(fn)


if __name__ == "__main__":
    main(sys.argv[1:])
