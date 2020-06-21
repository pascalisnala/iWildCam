# iWildCam 2019 - Android Edition

#### About iWildCam
iWildCam is a Kaggle competition which goal is to classify animal image data obtained from camera traps from all over Northwest and Southwest America. 

#### Project Goal

#### Possible Implementation

## Datasets
The dataset used in this project is the data from the 2019 version of the competition it self:
 - [iWildCam 2019 - FGVC6](https://www.kaggle.com/c/iwildcam-2019-fgvc6)

## Model Building
#### Baseline model
#### Improvement
#### Result

## Deployment

 - The model is deployed in an android app. The model is embedded in the app so it is downloaded and installed together with the app. We are using Tensorflow lite to embed to model to the app. This method chosen so the user can run this app offline!
 - The model is run on device CPU as this android app is not really depends on the computation speed. Anyway, the model still perform really well as it predicting in under then 200ms
 - The model is a quantized model as we are using a pre-trained model which usually very complex and heavy neural networks design. By doing this we are able to reduce the model size by 4 times. This make the application size is not huge while we can obtain almost the same result with the original model. 

## PPT
You can read more about this project in PPT format in this [link](www.google.com)

<br>

### Authors
*This project is part of Bangkit Program Final Project*
>Team Member: Nala Krisnanda, Fadel Nasution, Shelvia Andi

