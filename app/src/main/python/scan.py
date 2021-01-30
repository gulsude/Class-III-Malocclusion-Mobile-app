import face_alignment
from skimage import io
from skimage.transform import resize
import numpy as np
from PIL import ImageFont, ImageDraw, Image
import matplotlib.pyplot as plt
from os.path import dirname, join

def process(pic, save):

    print("inside, path:")
    print(" python code, flag 0")

    #2901
    fa = face_alignment.FaceAlignment(face_alignment.LandmarksType._2D, device='cpu')
    #fa = face_alignment.FaceAlignment(face_alignment.LandmarksType._2D, flip_input=False)
    input = io.imread(pic)

    print(" try ici, flag 1")
    preds = fa.get_landmarks(input)
    print(" try ici, flag 2")
    for i in preds: t = i
    sm_x = (t[11][0]+t[58][0])/2
    sm_y = (t[11][1]+t[58][1])/2
    sm = [sm_x,sm_y]

    a=[t[22],t[30],t[33],t[13],t[10],t[56],sm]
    ## G   Prn    Sn    Ls    Pg       Li  Sm
    preds=0
    fa=0
    t=0

    #### Calculate Ratio of Sn"-Pg"/G"Pg"
    ratio = (a[4][1]-a[2][1]) / (a[4][1]-a[0][1])
    add = 0
    if(ratio>=(0.55)):add = 2

    c3 = 0

    #### Angle1 G-Sn-Pg
    points = np.array([list(a[0]),list(a[2]),list(a[4])])
    A, B, C = (points[1] - points[0]) , (points[2] - points[1]) , (points[0] - points[2])
    angle1 = []
    for e1, e2 in ((A, -B), (B, -C), (C, -A)):
        num = np.dot(e1, e2)
        denom = np.linalg.norm(e1) * np.linalg.norm(e2)
        angle1.append(np.arccos(num/denom) * 180 / np.pi)
    if (((a[4][0]-a[2][0])+(a[0][0]-a[2][0]))>0): angle1[0] = (360 - angle1[0])


    #### Angle2 G-Prn-Pg
    points = np.array([list(a[0]),list(a[1]),list(a[4])])
    A, B, C = (points[1] - points[0]) , (points[2] - points[1]) , (points[0] - points[2])
    angle2 = []
    for e1, e2 in ((A, -B), (B, -C), (C, -A)):
        num = np.dot(e1, e2)
        denom = np.linalg.norm(e1) * np.linalg.norm(e2)
        angle2.append(np.arccos(num/denom) * 180 / np.pi)


    #### Angle3 G-Pg-Ls
    points = np.array([list(a[0]),list(a[4]),list(a[3])])
    A, B, C = (points[1] - points[0]) , (points[2] - points[1]) , (points[0] - points[2])
    angle3 = []
    for e1, e2 in ((A, -B), (B, -C), (C, -A)):
        num = np.dot(e1, e2)
        denom = np.linalg.norm(e1) * np.linalg.norm(e2)
        angle3.append(np.arccos(num/denom) * 180 / np.pi)


    #### Angle4 Li-Sm-Pg
    points = np.array([list(a[5]),list(a[6]),list(a[4])])
    A, B, C = (points[1] - points[0]) , (points[2] - points[1]) , (points[0] - points[2])
    angle4 = []
    for e1, e2 in ((A, -B), (B, -C), (C, -A)):
        num = np.dot(e1, e2)
        denom = np.linalg.norm(e1) * np.linalg.norm(e2)
        angle4.append(np.arccos(num/denom) * 180 / np.pi)
    if (((a[4][0]-a[6][0])+(a[5][0]-a[6][0]))<0): angle4[0] = (360 - angle4[0])

    print(" try ici, flag 3")
    angles = [(angle1[0] + add), (angle2[0] + add), (angle3[0]), (angle4[0])]

    ## Decision criterias
    if ((angles[0])>173.99): c3 = c3+1
    if ((angles[1])>147.71): c3 = c3+1
    if ((angles[2])<10.74): c3 = c3+1

    #Prediction
    prediction="SINIF III DEĞİL"
    if ((c3>1) & (angles[3]>138.69)): prediction="SINIF III"

    angles = [str(angle1[0] + add)[0:5], str(angle2[0] + add)[0:5], str(angle3[0])[0:4], str(angle4[0])[0:5]]

    for i in a: plt.scatter(i[0], i[1], s=5, c="red")
    plt.axis('off')
    plt.tick_params(axis='both', left='off', top='off', right='off', bottom='off', labelleft='off', labeltop='off', labelright='off', labelbottom='off')
    plt.imshow(input)

    #save = join(dirname(__file__), "outputs/ScannedImage.jpg")
    #print(text)
    #print(save)
    plt.savefig(save, dpi=600, bbox_inches='tight', pad_inches=0.0)

    rt = [prediction, angles, save]
    print("SUCCESS")
    return rt