#!/usr/bin/env python

template = """{
    "parent": "item/handheld",
    "textures": {
        "layer0": "roots:items/fey_pouch_main_layer",
        "layer1": "roots:items/strings/%s"
    }
}"""

colors = ["white","orange","magenta","light_blue","yellow","lime","pink","gray","silver","cyan","purple","blue","brown","green","red","black"]

for color in colors:
    filename = color + ".json"
    if color == "silver":
        color = "light_gray"

    with open(filename, "w") as o:
        o.write(template % color)