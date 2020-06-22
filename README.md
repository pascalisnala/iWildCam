
# iWildCam 2019 - Android 

#### About iWildCam
iWildCam is a Kaggle competition which goal is to classify animal image data obtained from camera traps from all over Northwest and Southwest America. 

#### Project Goal

 -  Learn how AI helps solving toughest environmental challenges
 - Learn about computer vision challenges in solving real-life problems

#### Possible Implementation in Indonesia
- Removing manual works in identifying animals in the pics, so wild life conservation scientists can spend more times in doing ecology analyses
- Tracking deforestation and its impact
- Raising awareness to public

## Datasets
The dataset used in this project is the data from the 2019 version of the competition itself. The dataset contain training set of images includes a total of 196,157 images from 138 different camera locations.

Comprises of **14 classes**: 
- bobcat
- opossum
- coyote
- raccoon
- dog
- cat
- squirrel
- rabbit
- skunk
- rodent
- deer
- fox
- mountain lion
- empty

[See More](https://www.kaggle.com/c/iwildcam-2019-fgvc6)

## Model Building
### Baseline model
We tried to train a basic CNN model  
| Layers| size|
|--|--|
| 2D Convolutions| (32,32,32)
| Pooling  | (16,16,32) |
| 2D Covolutions| (14,14,32) |
| 2D Covolutions| (6272) |
| Dense (output)| (14) |

**Other information:** 
- Not using any image augmentation
- batchsize = 32
- Adam Optimizer
- Categorical CE loss function

**Result:**
validation loss = 0.687
validation accuracy = 0.8096

### Improvement
**Using Pretrained Model**

- EfficientNetB0
- EfficientNetB3
- EfficientNetB6
- DenseNet

**Fine tuning pretrained model**
(**Selected : with EfficientNetB0 with unfreeze layer block6a_expand_conv**)
- Experimenting with appearance of Dropout layers **(selected: add dropout)**
- Callback experiment including early stopping and LR on plateu
- input image size experiment (**selected: 128x128**)
- Optimizers experiment (**selected: Adam**)
- Batch size experiment (**selected: 64**)

### Result
**validation loss = 0.4516
validation accuracy = 0.8540**

## Deployment

 - The model is deployed in an android app. The model is embedded in the app so it is downloaded and installed together with the app. We are using Tensorflow lite to embed to model to the app. This method chosen so the user can run this app offline!
 - The model is run on device CPU as this android app is not really depends on the computation speed. Anyway, the model still perform really well as it predicting in under than 200ms
 - The model is a quantized model as we are using a pre-trained model which usually very complex and heavy neural networks design. By doing this we are able to reduce the model size by 4 times. This make the application size is not huge while we can obtain almost the same result with the original model. 

## PPT
You can read more about this project in PPT format in this [link](https://drive.google.com/file/d/1AkwS5_mD-hmTGgWWblrqbtbzjN1q3Su3/view?usp=sharing)


## Using the APP
You can clone/pull this repo and try to open "Android/WildCam" folder in the Android Studio and now you can edit or build this project

Or you can simply install the application APK file from [here ](https://drive.google.com/file/d/13qNmup-5UKcfrfcJgjwNlT06352NugG9/view?usp=sharing)

<br>

### Authors
*This project is part of Bangkit Program Final Project*
>Team Member: Nala Krisnanda, Fadel Nasution, Shelvia Andi

