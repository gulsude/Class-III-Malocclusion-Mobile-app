import face_alignment
from skimage import io
import numpy as np
import os
from PIL import ImageFont, ImageDraw, Image
import matplotlib.pyplot as plt
from os.path import dirname, join


def process(pic):
    pic = join(dirname(__file__), pic)
    print(pic)
    basewidth = 556
    p = Image.open(pic)
    wpercent = (basewidth / float(p.size[0]))
    hsize = int((float(p.size[1]) * float(wpercent)))
    p = p.resize((basewidth, hsize), Image.ANTIALIAS)

    rgb_im = p.convert('RGB')
    path = pic.split(".png")
    pic_v1 = path[0] + "_v1" + '.jpg'
    pic_v2 = path[0] + "_v2" + '.jpg'
    print("before save", pic_v2)
    rgb_im.save(pic_v2)

    ##raw images klasorune kaydetsek cok iyi olur


    fa = face_alignment.FaceAlignment(face_alignment.LandmarksType._2D, flip_input=False)
    input = io.imread(pic_v2)
    print(" try ici, NOKTA 1")

    preds = fa.get_landmarks(input)
    for i in preds: t = i
    a = [t[22], t[30], t[33], t[13], t[10]]

    #### Calculate Ratio of Sn"-Pg"/G"Pg"
    ratio = (a[4][1] - a[2][1]) / (a[4][1] - a[0][1])
    add = 0
    if ratio >= (0.55): add = 2

    #### Write Classes on Images
    img = Image.open(pic_v2)
    draw = ImageDraw.Draw(img)
    font = ImageFont.truetype("text.otf", 16)

    angles = []
    c3 = 0

    #### Angle1 G-Sn-Pg
    points = np.array([list(a[0]), list(a[2]), list(a[4])])
    A, B, C = (points[1] - points[0]), (points[2] - points[1]), (points[0] - points[2])
    angle1 = []
    for e1, e2 in ((A, -B), (B, -C), (C, -A)):
        num = np.dot(e1, e2)
        denom = np.linalg.norm(e1) * np.linalg.norm(e2)
        angle1.append(np.arccos(num / denom) * 180 / np.pi)
    if (((a[2][0] - a[1][0]) + (a[0][0] - a[1][0])) > 0):
        angle1[0] = (360 - angle1[0])

        #### Angle2 G-Prn-Pg
    points = np.array([list(a[0]), list(a[1]), list(a[4])])
    A, B, C = (points[1] - points[0]), (points[2] - points[1]), (points[0] - points[2])
    angle2 = []
    for e1, e2 in ((A, -B), (B, -C), (C, -A)):
        num = np.dot(e1, e2)
        denom = np.linalg.norm(e1) * np.linalg.norm(e2)
        angle2.append(np.arccos(num / denom) * 180 / np.pi)
    if (((a[2][0] - a[1][0]) + (a[0][0] - a[1][0])) > 0):
        angle2[0] = (360 - angle2[0])
    print(" try ici, NOKTA 2")
    #### Angle3 G-Pg-Ls
    points = np.array([list(a[0]), list(a[4]), list(a[3])])
    A, B, C = (points[1] - points[0]), (points[2] - points[1]), (points[0] - points[2])
    angle3 = []
    print("line 77")
    for e1, e2 in ((A, -B), (B, -C), (C, -A)):
        num = np.dot(e1, e2)
        denom = np.linalg.norm(e1) * np.linalg.norm(e2)
        angle3.append(np.arccos(num / denom) * 180 / np.pi)

    angles = [(angle1[0] + add), (angle2[0] + add), angle3[0]]
    print("line 84")
    if ((angles[0]) > 173.99): c3 = c3 + 1
    if ((angles[1]) > 147.71): c3 = c3 + 1
    if ((angles[2]) < 10.74): c3 = c3 + 1

    prediction = "SINIF III DEGIL"
    if (c3 > 1): prediction = "SINIF III"

    draw.rectangle((10, 400, 325, 340), fill='black')

    draw.rectangle((10, 460, 110, 410), fill='black')
    print("line 95")
    draw.text((20, 345), "G-Sn-Pg", font=font)
    draw.text((135, 345), "G-Prn-Pg", font=font)
    draw.text((255, 345), "G-Pg-Ls", font=font)
    print(" try ici, NOKTA 3")
    angle1 = str(angles[0])[0:6]
    angle2 = str(angles[1])[0:6]
    angle3 = str(angles[2])[0:5]
    print("line 103")
    draw.text((20, 370), angle1, font=font)
    draw.text((135, 370), angle2, font=font)
    draw.text((255, 370), angle3, font=font)

    draw.text((20, 415), "result:", font=font)
    draw.text((20, 435), prediction, font=font)
    for i, name in zip(a, ["G", "Prn", "Sn", "Ls", "Pg"]): draw.text((i[0] + 1, i[1]), name, font=font)
    plt.imshow(img)

    for i in a: plt.scatter(i[0], i[1], s=5, c="red")
    plt.axis('off')
    plt.tick_params(axis='both', left='off', top='off', right='off', bottom='off', labelleft='off', labeltop='off',
                    labelright='off', labelbottom='off')
    plt.imshow(img)
    plt.savefig("ScannedImage.jpg", dpi=1200, bbox_inches='tight', pad_inches=0.0)

    rt = [prediction, pic_v2]
    print(" SUCCESS")

    plt.clf()
    plt.cla()
    plt.close()
    return rt
