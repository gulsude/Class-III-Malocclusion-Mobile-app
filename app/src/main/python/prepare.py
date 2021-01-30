import face_alignment
from skimage import io
from skimage.transform import resize
import numpy as np
from PIL import ImageFont, ImageDraw, Image
import matplotlib.pyplot as plt
from os.path import dirname, join

def process():

    print("inside, prepare:")
    try:
        fa = face_alignment.FaceAlignment(face_alignment.LandmarksType._2D, device='cpu')
    except:
        print("inside, cannot repare:")
