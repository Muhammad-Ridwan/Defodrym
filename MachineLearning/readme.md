# Defodrym - Machine Learning Notebook
 
## Introduction
This repo contain's 1 model Foodictive Machine Learning. The models contain 9 classes of food.

## Our Machine Learning Team
- M246DSX311– Muhammad Ridwan
- M037DSY0522 – Rizka Arfie Amaliya

## Dataset
- (https://www.kaggle.com/datasets/faldoae/padangfood)

## How To Use
### Deploy to localhost 
1. Clone the repository 
   ```
   git clone https://github.com/avocadojj/Defodyrm-ML.git
   ```
2. open terminal and move to this directory
   ```
   cd Defodrym-ML
   ```
3. Use anaconda or create virtual environment
   ```
   python -m venv venv
   ```
4. For windows, Activate virtual environment
   ```
   venv\Scripts\activate (windows)
   source venv/bin/activate (Mac / Linux) 
   ```
5. Install the requirements
   ```
   python -m pip install -r requirements.txt
   ```
6. Run the app
   ```
   python app.py
   ```
### Applied .tflite to Android
The model in this repository is designed to run on android applications. The following is a documentation guide for using the .tflite model in android studio (We use kotlin)
1. Do instance your model in model variable
   ```
   val model = YourModel.newInstance(this)
   ```
2. Take image in bitmap then insert to image variable
   ```
   val image = TensorImage.fromBitmap(bitmap)
   ```
3. Do prediction on image, use list for descending then give list to outputs variable.
   ```
   val outputs = model.process(image).probabilityAsCategoryList.apply {
      sortByDescending { it.score }
   }
   ```
4. Access outputs[0] for class that have biggest accuracy then give to probability variable
   ```
   val probability = outputs[0]
   ```
   
Here is the full code
```
val model = YourModel.newInstance(this)
val image = TensorImage.fromBitmap(bitmap)

val outputs = model.process(image).probabilityAsCategoryList.apply {
    sortByDescending { it.score }
}
val probability = outputs[0]
```
        
## Contributing
We are open and grateful to anyone who wants to contribute to the development of this machine learning model in order to get better results. Here are the steps you can take
1. Fork this repository.
2. Clone the forked repository to your machine.
3. Commit changes to your own branch.
4. Push your work back up to your fork.
5. Submit a Pull request for review.
6. Wait until we review your change.
