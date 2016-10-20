# Squint
Provide Diagonal cut on view with awesome customization #DiagonalView

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Squint%20--%20A%20Diagonal%20Cut-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4458) [![API](https://img.shields.io/badge/API-9%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=9) [![](https://jitpack.io/v/IntruderShanky/Squint.svg)](https://jitpack.io/#IntruderShanky/Squint)

# Preview
![Screenshot](Screenshot/screenshot.png)

# Usage
Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```
Step 2. Add the dependency
```groovy
dependencies {
  compile 'com.github.IntruderShanky:Squint:2.1.0'
 }
 ```
# Implementation
###XML Implementation:
```xml
 <com.intrusoft.squint.DiagonalView
        android:id="@+id/diagonal"
        android:layout_width="match_parent"
        android:layout_height="350dp"
        squint:angle="15"
        squint:gravity="left"
        squint:scaleType="centerCrop"
        squint:solidColor="@color/solid_color"
        squint:src="@drawable/barney_cover" />
```
###Attributes
####Diagonal Angle
```xml
 squint:angle
```
####Diagonal Gravity
```xml
squint:gravity="left"
squint:gravity="right"
```
####Image ScaleType
```xml
squint:scaleType="centerCrop"
squint:scaleType="fitXY"
```
####Image Drawable Resource
```xml
squint:src="@drawable/your_image"
```
####Background Tint Color (Color Shold have some alpha value, default value 55)
```xml
squint:tint="@color/your_color"
```
####To make solid color Diagonal
```xml
squint:solidColor="@color/your_color"
```
###Java Implementation:
```java
DiagonalView diagonalView = (DiagonalView) findViewById(R.id.diagonal);

// to set image from resources        
diagonalView.setImageSource(R.drawable.your_image);

// to set bitmap
diagonalView.setBitmap(bitmap);

// to set the diagonal angle
diagonalView.setAngle(15);

// to set the diagonal gravity
diagonalView.setGravity(DiagonalView.Gravity.LEFT);

// to set the background color (color should have some alpha val)
diagonalView.setColorTint(Color.GREEN);

// to make the solid color diagonal
diagonalView.setSolidColor(Color.BLUE);
```
#Licence
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
