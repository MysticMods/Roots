#!/usr/bin/env python

import sys
import re
from itertools import tee, zip_longest

def grouper(iterable, n, fillvalue=None):
    "Collect data into fixed-length chunks or blocks"
    # grouper('ABCDEFG', 3, 'x') --> ABC DEF Gxx"
    args = [iter(iterable)] * n
    return zip_longest(*args, fillvalue=fillvalue)

def parse (filename):
    with open(filename) as o:
        data = o.readlines()

    lines = [l for l in data if '"from"' in l or '"to"' in l]
    new_lines = []
    for from_line, to_line in grouper(lines, 2):
        new_lines.append("Block.box("+from_line.split(": [")[1].replace("]", "").strip()+to_line.split(": [")[1].replace("]", ")").strip())

    print("VoxelUtil.multiOr(" + " ".join(new_lines).strip(",") + ");")

def main (args):
    for fn in args:
        if fn.endswith(".json"):
            parse(fn)

if __name__=="__main__":
    main(sys.argv)
