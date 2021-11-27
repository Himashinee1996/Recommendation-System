#!/usr/bin/env python
# coding: utf-8

# In[36]:


# Import libraries
import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt
import os
from cv2 import cv2
import random
from sklearn.model_selection import train_test_split
from tensorflow.keras.utils import to_categorical
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import  Conv2D, MaxPooling2D,Dense, Dropout, Flatten, Activation, BatchNormalization
from tensorflow.keras.datasets import cifar10
import tensorflow.keras.utils
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.callbacks import EarlyStopping
import numpy as np
from scipy import misc
from tensorflow.keras.models import load_model
import imutils
import pickle
from PIL import Image,ImageEnhance
from tensorflow import keras
import pandas as pd


# In[37]:


# defining the path for datasets.
dataset_path = 'images'
# define classes
classes = ['Doll','Guns','Remote_car']


# In[38]:


# defining the image sizes
imgSize1 = 240
imgSize2 = 240
figure_size = 9 


# In[39]:


# remove noises from images using median filter
for cls in classes:
    path = os.path.join(dataset_path,cls)
    class_index = classes.index(cls)
    
    for img in os.listdir(path):
        image = cv2.imread(os.path.join(path,img))
        image = cv2.resize(image,(imgSize1,imgSize2))
        new_image = cv2.medianBlur(image, figure_size)
plt.figure(figsize=(11,6))
plt.subplot(121), plt.imshow(cv2.cvtColor(image, cv2.COLOR_HSV2RGB)),plt.title('Original')
plt.xticks([]), plt.yticks([])
plt.subplot(122), plt.imshow(cv2.cvtColor(new_image, cv2.COLOR_HSV2RGB)),plt.title('Median Filter')
plt.xticks([]), plt.yticks([])
plt.show()


# In[40]:


img_data = []

# remove noises using gaussian filter and create final image dataset
def createDataset():
    for cls in classes:
        path = os.path.join(dataset_path,cls)
        class_index = classes.index(cls)
    
        for img in os.listdir(path):
            image = cv2.imread(os.path.join(path,img))
            #resize image
            new_image = cv2.resize(image,(imgSize1,imgSize2))
            #convert to grayscale
            new_image = cv2.cvtColor(new_image, cv2.COLOR_BGR2GRAY)
            #remove noises
            new_image = cv2.GaussianBlur(new_image, (figure_size, figure_size),0)
            img_data.append([new_image,class_index])
    plt.figure(figsize=(11,6))
    plt.subplot(121), plt.imshow(image, cmap='gray'),plt.title('Original')
    plt.xticks([]), plt.yticks([])
    plt.subplot(122), plt.imshow(new_image, cmap='gray'),plt.title('Gaussian Filter')
    plt.xticks([]), plt.yticks([])
    plt.show()
            


# In[41]:


createDataset()


# In[42]:


print(len(img_data))


# In[43]:


# shuffle dataset
random.shuffle(img_data)


# In[44]:


x = []
y = []


# In[45]:


# extract features and label from image dataset
for features, label in img_data:
    x.append(features)
    y.append(label)


# In[46]:


x[10].shape


# In[47]:


len(x)


# In[48]:


# convert dataset into numpy array
X = np.array(x).reshape(-1,imgSize1,imgSize2,1)
X[0].shape
len(X)


# In[49]:


# split dataset into training and testing data
x_train,x_test,y_train,y_test = train_test_split(X,y,test_size=0.3,random_state=50)


# In[50]:


# convert dataset labels to categorical
Y_train = to_categorical(y_train,num_classes=3)
Y_test = to_categorical(y_test,num_classes=3)


# In[51]:


# define model layers
model = keras.Sequential([
    keras.layers.Flatten(input_shape=(240, 240)),
    keras.layers.Dense(128, activation=tf.nn.relu),
    keras.layers.Dense(3, activation=tf.nn.softmax)
])


# In[52]:


model.compile(optimizer='adam',loss='categorical_crossentropy',metrics=['accuracy'])


# In[53]:


model.summary()


# In[54]:


n_epochs =20


# In[55]:


results = model.fit(x_train,Y_train,epochs=n_epochs,verbose=1,validation_data=(x_test,Y_test))


# In[56]:


# model accuracy
plt.plot(results.history['accuracy'])
plt.plot(results.history['val_accuracy'])
plt.title('Model accuracy')
plt.ylabel('Accuracy')
plt.xlabel('Epoch')
plt.legend(['Train', 'Test'], loc='upper left')
plt.show()

# model loss
plt.plot(results.history['loss'])
plt.plot(results.history['val_loss'])
plt.title('Model loss')
plt.ylabel('Loss')
plt.xlabel('Epoch')
plt.legend(['Train', 'Test'], loc='upper left')
plt.show()


# # Evaluate the model

# In[57]:


test_loss, test_acc = model.evaluate(x_test,Y_test)
print("Test accuracy:", test_acc)


# # Save the model

# In[58]:


model.save('./toy-detection.h5')


# # Test the model

# In[59]:


loaded_model = load_model('toy-detection.h5')


# In[60]:


testing_dataset = 'testing'
testing_data = []
labels = []

for img in os.listdir(testing_dataset):
    labels.append(img)
    imgArray = cv2.imread(os.path.join(testing_dataset,img),cv2.IMREAD_GRAYSCALE)
    newImg = cv2.resize(imgArray,(imgSize1,imgSize2))
    testing_data.append([newImg])


# In[61]:


print(labels)


# In[62]:


test_img = testing_data[12]
test_data = np.array(test_img).reshape(-1,imgSize1,imgSize2,1)


# In[63]:


predection = loaded_model.predict(test_data)
np.argmax(predection)


# In[64]:


product_name = ""

if(np.argmax(predection) == 0):
    print("Type: Doll")
    product_name = "Doll"
if(np.argmax(predection) == 1):
    print("Type: Guns")
    product_name = "Guns"
if(np.argmax(predection) == 2):
    print("Type: Remote Car")
    product_name = "Remote Cars"


# # Recommend Similar product

# In[65]:


#Read the dataset
sd_data = pd.read_csv("rating_dataset.csv", encoding= 'unicode_escape')

#Display the data
sd_data.head()


# In[66]:


df = pd.DataFrame(sd_data)


# In[67]:


df = df.sort_values('Ratings',ascending=False)


# In[68]:


df.head()


# In[69]:


similar_items = []
similar_items_with_ratings = []

# Get similar Items
for index, row in df.iterrows():
    if(row['Product Name'] == product_name):
        similar_items.append(row['Item code'])
        similar_items_with_ratings.append([row['Item code'],row['Ratings']])
   
# Display similar items list with ratings
similar_items_with_ratings


# In[70]:


# Display Similar Products
path = "testing"

for img in os.listdir(path):
    for itemCode in similar_items:
        if(img.split('.')[0] == itemCode):
            image = cv2.imread(os.path.join(path,img))       
            plt.figure(figsize=(11,6))
            plt.subplot(121), plt.imshow(image),plt.title(img.split('.')[0])
            plt.xticks([]), plt.yticks([])
            plt.show()
            


# In[ ]:




