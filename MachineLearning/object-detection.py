#!/usr/bin/env python
# coding: utf-8

# In[1]:


# This Python 3 environment comes with many helpful analytics libraries installed
# It is defined by the kaggle/python Docker image: https://github.com/kaggle/docker-python
# For example, here's several helpful packages to load

import numpy as np # linear algebra
import pandas as pd # data processing, CSV file I/O (e.g. pd.read_csv)

# Input data files are available in the read-only "../input/" directory
# For example, running this (by clicking run or pressing Shift+Enter) will list all files under the input directory

import os
for dirname, _, filenames in os.walk('/kaggle/input'):
    for filename in filenames:
        print(os.path.join(dirname, filename))

# You can write up to 20GB to the current directory (/kaggle/working/) that gets preserved as output when you create a version using "Save & Run All" 
# You can also write temporary files to /kaggle/temp/, but they won't be saved outside of the current session


# In[3]:


import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers,models
from keras.preprocessing.image import ImageDataGenerator
from keras.layers import Dense, Dropout
from tensorflow.keras.callbacks import Callback, EarlyStopping,ModelCheckpoint
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras import Model
from tensorflow.keras.layers.experimental import preprocessing
import random
import cv2
import json
from pathlib import Path
import os.path
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.model_selection import train_test_split
import itertools
import matplotlib.pyplot as plt
import matplotlib.cm as cm


# In[4]:


get_ipython().system('wget https://raw.githubusercontent.com/mrdbourke/tensorflow-deep-learning/main/extras/helper_functions.py')

from helper_functions import create_tensorboard_callback, plot_loss_curves, unzip_data, compare_historys, walk_through_dir, pred_and_plot


# In[5]:


# Load the data
dataset = "../input/padangfood/dataset_padang_food"
walk_through_dir(dataset)


# In[6]:


# Adding data to the Dataframe
image_dir = Path(dataset)
filepaths = list(image_dir.glob(r'**/*.JPG')) + list(image_dir.glob(r'**/*.jpg')) + list(image_dir.glob(r'**/*.png')) + list(image_dir.glob(r'**/*.png'))
labels = list(map(lambda x: os.path.split(os.path.split(x)[0])[1], filepaths))
filepaths = pd.Series(filepaths, name='Filepath').astype(str)
labels = pd.Series(labels, name='Label')

image_df = pd.concat([filepaths, labels], axis=1)
image_df


# In[7]:


# Image visualization from the dataset
random_index = np.random.randint(0, len(image_df), 16)
fig, axes = plt.subplots(nrows=4, ncols=4, figsize=(10, 10),
                        subplot_kw={'xticks': [], 'yticks': []})

for i, ax in enumerate(axes.flat):
    ax.imshow(plt.imread(image_df.Filepath[random_index[i]]))
    ax.set_title(image_df.Label[random_index[i]])
plt.tight_layout()
plt.show()


# In[23]:


# This function computes the Error Level Analysis (ELA) of an image
def compute_ela_cv(path, quality):
    
    # Create temporary filename
    temp_filename = 'temp_filename.jpeg'
    
    # define the number of scales to use
    scales = 15
    
    # read original image
    orginal_img = cv2.imread(path)
    
    # convert the original image to RGB
    orginal_img = cv2.cvtColor(orginal_img, cv2.COLOR_BGR2RGB)
    
    # Save the original image with the specified JPEG quality
    cv2.imwrite(temp_filename, orginal_img, [cv2.IMWRITE_JPEG_QUALITY, quality])
    
    # read compressed image
    compressed_img = cv2.imread(temp_filename)
    
    # Multiply the absolute difference between the original and compressed photos by the scaling factor.
    diff = scales * cv2.absdiff(orginal_img, compressed_img)
    
    #return diff
    return diff

# this function converts an image to its ELA representation
def convert_to_ela_image(path, quality):
    temp_filename = 'temp_filename.jpeg'
    ela_filename = 'temp_ela.png'
    
    # Open the image and convert it to RGB format
    image = Image.open(path).convert('RGB')
    
    # Save the image with the specified JPEG quality
    image.save(temp_filename, 'JPEG', quality = quality)
    
    # Open the temporary image and compute the difference between the original and compressed images
    temp_image = Image.open(temp_filename)
    ela_image = ImageChops.difference(image, temp_image)
    
    # determine the greatest difference between the pixels in the ELA image.
    extrema = ela_image.getextrema()
    max_diff = max([ex[1] for ex in extrema])
    
# returns a random file with the provided extension from a directory.
def random_sample(path, extension=None):
    if extension:
        items = Path(path).glob(f'*.{extension}')
    else:
        items = Path(path).glob(f'*')     
    items = list(items)    
    random_img = random.choice(items)
    return random_img.as_posix()


# In[24]:


# Get a random image from the dataset
random_img = random_sample('../input/padangfood/dataset_padang_food/ayam_pop')
orginal_img = cv2.imread(random_img)
orginal_img = cv2.cvtColor(org, cv2.COLOR_BGR2RGB) / 255.0
# Set initial quality value and number of rows and columns for the plot
init = 100
columns = 3
rows = 3
# Create a plot to display the images
fig=plt.figure(figsize=(15, 10))
for i in range(1, columns * rows + 1):
    # Calculate the quality value for the current image
    quality = init - (i - 1) * 8
    # Apply the quality degradation effect to the image
    degraded_img = compute_ela_cv(path = random_img,quality = quality)
    if i == 1:
        degraded_img = org.copy()
    # Add the image to the plot
    ax = fig.add_subplot(rows, columns, i) 
    ax.title.set_text(f'q: {quality}')
    plt.imshow(degraded_img)
plt.show()


# In[25]:


# Separate in train and test data
train_df, test_df = train_test_split(image_df, 
                                     test_size=0.2, 
                                     shuffle=True, 
                                     random_state=42)
train_generator = ImageDataGenerator(
    preprocessing_function=tf.keras.applications.mobilenet_v3.preprocess_input,
    validation_split=0.2
)

test_generator = ImageDataGenerator(
    preprocessing_function=tf.keras.applications.mobilenet_v3.preprocess_input
)


# In[26]:


# Split the data into three categories.
train_images = train_generator.flow_from_dataframe(
    dataframe=train_df,
    x_col='Filepath',
    y_col='Label',
    target_size=(224, 224),
    color_mode='rgb',
    class_mode='categorical',
    batch_size=32,
    shuffle=True,
    seed=42,
    subset='training'
)


# In[27]:


#validation
val_images = train_generator.flow_from_dataframe(
    dataframe=train_df,
    x_col='Filepath',
    y_col='Label',
    target_size=(224, 224),
    color_mode='rgb',
    class_mode='categorical',
    batch_size=32,
    shuffle=True,
    seed=42,
    subset='validation'
)


# In[28]:


#testing
test_images = test_generator.flow_from_dataframe(
    dataframe=test_df,
    x_col='Filepath',
    y_col='Label',
    target_size=(224, 224),
    color_mode='rgb',
    class_mode='categorical',
    batch_size=32,
    shuffle=False
)


# In[29]:


# Resize Layer
resize_and_rescale = tf.keras.Sequential([
  layers.experimental.preprocessing.Resizing(224,224),
  layers.experimental.preprocessing.Rescaling(1./255),
])

# Load the pretained model
pretrained_model = tf.keras.applications.MobileNetV3Large(
    input_shape=(224, 224, 3),
    include_top=False,
    weights='imagenet',
    pooling='avg'
)

pretrained_model.trainable = False

# Create checkpoint callback
checkpoint_path = "foods_classification_model_checkpoint"
checkpoint_callback = ModelCheckpoint(checkpoint_path,
                                      save_weights_only=True,
                                      monitor="val_accuracy",
                                      save_best_only=True)

# Setup EarlyStopping callback
early_stopping = EarlyStopping(monitor = "val_loss",
                               patience = 5,
                               restore_best_weights = True)


# In[31]:


# Define the input layer of the neural network model
input_layer_nn = pretrained_model.input

# resize and rescale the input images
rescaled_images_nn = resize_and_rescale(input_layer_nn)

# Add dense layers with ReLU activation and dropout regularization to the neural network model
dense_layer_1_nn = Dense(256, activation='relu')(pretrained_model.output)
dropout_layer_1_nn = Dropout(0.2)(dense_layer_1_nn)
dense_layer_2_nn = Dense(256, activation='relu')(dropout_layer_1_nn)
dropout_layer_2_nn = Dropout(0.2)(dense_layer_2_nn)

# Define the output layer of the neural network model with softmax activation
output_layer_nn = Dense(9, activation='softmax')(dropout_layer_2_nn)

# Define the neural network model with the input and output layers
model_nn = Model(inputs=input_layer_nn, outputs=output_layer_nn)

# Compile the neural network model with Adam optimizer, categorical crossentropy loss and accuracy metric
model_nn.compile(
    optimizer=Adam(0.00001),
    loss='categorical_crossentropy',
    metrics=['accuracy']
)

# Train the nn model with the training and validation data, using early stopping, tensorboard and checkpoint callbacks
history_nn = model_nn.fit(
    train_images,
    steps_per_epoch=len(train_images),
    validation_data=val_images,
    validation_steps=len(val_images),
    epochs=100,
    callbacks=[
        early_stopping,
        create_tensorboard_callback("training_logs", 
                                    "food_classification"),
        checkpoint_callback,
    ]
)


# In[35]:


# model evaluation
results = model_nn.evaluate(test_images, verbose=0)

print("    Test Loss: {:.5f}".format(results[0]))
print("Test Accuracy: {:.2f}%".format(results[1] * 100))

plot_loss_curves(history_nn)


# In[37]:


# Predict the label of the test_images
pred = model_nn.predict(test_images)
pred = np.argmax(pred,axis=1)
# Map the label
labels = (train_images.class_indices)
labels = dict((v,k) for k,v in labels.items())
pred1 = [labels[k] for k in pred]
pred1


# In[40]:


from tensorflow.keras.preprocessing.image import load_img,img_to_array
def output(location):
    img = load_img(location,target_size=(224,224,3))
    img = img_to_array(img)
    img = img/255
    img = np.expand_dims(img,[0])
    answer = model_nn.predict(img)
    y_class = answer.argmax(axis=-1)
    y = " ".join(str(x) for x in y_class)
    y = int(y)
    res = labels[y]
    return res


# In[45]:


img = output('/kaggle/input/padangfood/dataset_padang_food/gulai_tambusu/gulai_tambusu (100).jpg')
img


# In[46]:


# predict the label of the test_images
pred = model_nn.predict(test_images)
pred = np.argmax(pred,axis=1)

# Map the label
labels = (train_images.class_indices)
labels = dict((v,k) for k,v in labels.items())
pred = [labels[k] for k in pred]

# Display the result
print(f'The first 5 predictions: {pred[:5]}')


# In[48]:


# display some random pictures from the dataset with their labels
random_index = np.random.randint(0, len(test_df) - 1, 15)
fig, axes = plt.subplots(nrows=3, 
                         ncols=5, 
                         figsize=(25, 15),
                        subplot_kw={'xticks': [], 'yticks': []})

for i, ax in enumerate(axes.flat):
    ax.imshow(plt.imread(test_df.Filepath.iloc[random_index[i]]))
    if test_df.Label.iloc[random_index[i]] == pred[random_index[i]]:
      color = "green"
    else:
      color = "red"
    ax.set_title(f"True: {test_df.Label.iloc[random_index[i]]}\nPredicted: {pred[random_index[i]]}", color=color)
plt.show()
plt.tight_layout()

y_test = list(test_df.Label)
print(classification_report(y_test, pred))


# In[49]:


# shows classification report
report = classification_report(y_test, pred, output_dict=True)
df = pd.DataFrame(report).transpose()
df


# In[56]:


def make_confusion_matrix(y_true, 
                          y_pred, 
                          classes=None, 
                          figsize=(15, 7), 
                          text_size=10, 
                          norm=False, 
                          savefig=False): 
    cm = confusion_matrix(y_true, y_pred)
    cm_norm = cm.astype("float") / cm.sum(axis=1)[:, np.newaxis] # normalize it
    n_classes = cm.shape[0]
    
    # plot the figure and make it pretty
    fig, ax = plt.subplots(figsize=figsize)
    
    # colors will represent how correct a class is, darker means better
    cax = ax.matshow(cm, cmap=plt.cm.Blues)
    fig.colorbar(cax)
    if classes:
        labels = classes
    else:
        labels = np.arange(cm.shape[0])
        
    # Label the axes
    ax.set(title="Confusion Matrix",
         xlabel="Predicted label",
         ylabel="True label",
         xticks=np.arange(n_classes),
           
        # create enough axis slots for each class
         yticks=np.arange(n_classes), 
         xticklabels=labels,
           
        # axes will labeled with class names (if they exist) or ints
         yticklabels=labels)
  
    # Make x-axis labels appear on bottom
    ax.xaxis.set_label_position("bottom")
    ax.xaxis.tick_bottom()
    
    # added rotate xticks for readability & increase font size
    plt.xticks(rotation=90, fontsize=text_size)
    plt.yticks(fontsize=text_size)

    # Set the threshold for different colors
    threshold = (cm.max() + cm.min()) / 2.
    
    # Plot the text on each cell
    for i, j in itertools.product(range(cm.shape[0]), range(cm.shape[1])):
        if norm:
            plt.text(j, i, f"{cm[i, j]} ({cm_norm[i, j]*100:.1f}%)",
                horizontalalignment="center",
                color="white" if cm[i, j] > threshold else "black",
                size=text_size)
        else:
            plt.text(j, i, f"{cm[i, j]}",
              horizontalalignment="center",
              color="white" if cm[i, j] > threshold else "black",
              size=text_size)

  # Save the figure to the current working directory
    if savefig:
        fig.savefig("confusion_matrix.png")

# show the graphic
make_confusion_matrix(y_test, pred, list(labels.values()))


# In[58]:


#Save the trained model as a Keras HDF5 file. 

saved_model_path = "./my_model.h5"
model_nn.save(saved_model_path)


# In[59]:


# convert model to tflite model
model = tf.keras.models.load_model('my_model.h5')
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()
open("converted_model.tflite", "wb").write(tflite_model)

