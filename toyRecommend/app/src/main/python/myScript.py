import numpy as np
from cv2 import cv2
import base64
import io
from tensorflow.keras.models import load_model
from os.path import dirname, join

filename = join(dirname(__file__), "toy-detection.h5")

def main(data):
    decoded_data = base64.b64decode(data)
    np_data = np.fromstring(decoded_data, np.uint8)
    img = cv2.imdecode(np_data,cv2.IMREAD_UNCHANGED)
    img_gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    newImg = cv2.resize(img_gray,(240,240))
    test_data = np.array(newImg).reshape(1,240,240)
    #pil_img = Image.fromarray(img_gray)

    # buff = io.BytesIO()
    # pil_img.save(buff,format="PNG")
    # img_str = base64.b64encode(buff.getvalue())

    loaded_model = load_model(filename)
    prediction = loaded_model.predict(test_data)
    np.argmax(prediction)

    product_name = ""

    if(np.argmax(prediction) == 0):
        product_name = "Doll"
    elif(np.argmax(prediction) == 1):
        product_name = "Gun"
    elif(np.argmax(prediction) == 2):
        product_name = "Remote Car"
    else:
        product_name = "Not Found"

    return ""+str(product_name)

def getAddOns(data):
    print("dhasgkhdsgfhiagfiahglwhdsfjaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
    pairs = {
        "GU12" : "GUCA11",
        "GU25" : "GUCA11",
        "RC13" : "RCBA10",
        "DO10" : "DOHA02",
        "RC07" : "RCBA10"
    }

    values = data.split("/");
    addOns = ""

    for key, value in pairs.items():
        for val in values:
            if (val == key):
                addOns += value + "/"
    return addOns