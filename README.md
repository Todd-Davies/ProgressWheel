Progress Wheel
=============

This is a custom component for Android intended for use instead of a progress bar.

![Sample Image](https://github.com/Todd-Davies/ProgressWheel/raw/master/sample_image.png "An example implementation")
![Sample Image 2](https://github.com/Todd-Davies/ProgressWheel/raw/master/sample_image_2.png "Another example implementation")
![Sample Image 3](https://github.com/Todd-Davies/ProgressWheel/raw/master/sample_image_3.png "Another example implementation")
![Sample Image 4](https://github.com/Todd-Davies/ProgressWheel/raw/master/sample_image_4.png "Another example implementation")

A complete walkthrough of how to use this component in your app
-------------

**XML:**   
To implement the view in your xml layout do the following:

1. Add the following to your attrs.xml file (in res/values):
``` xml
<declare-styleable name="ProgressWheel">   
	<attr name="text" format="string" />   
	<attr name="textColor" format="color" />   
	<attr name="textSize" format="dimension" />   
	<attr name="barColor" format="color" />   
	<attr name="rimColor" format="color" />   
	<attr name="rimWidth" format="dimension" />   
	<attr name="spinSpeed" format="integer" />     
	<attr name="circleColor" format="color" />     
	<attr name="radius" format="dimension" />   
	<attr name="barWidth" format="dimension" />   
	<attr name="barLength" format="dimension" />
	<attr name="delayMillis" format="dimension"/>
	<attr name="contourColor" format="color"/>
	<attr name="contourSize" format="float"/>
</declare-styleable> 
```

2. Add the following code to the root view of your layout:
`xmlns:ProgressWheel="http://schemas.android.com/apk/res/com.visualdenim.schooltraq"`

3. Add the widget code in the appropriate place in your xml file. Here's a sample implementation:
``` xml
<com.todddavies.components.progressbar.ProgressWheel   
    android:id="@+id/pw_spinner"     
    android:layout_width="200dp"    
    android:layout_height="200dp"   
    android:layout_centerInParent="true"   
    ProgressWheel:text="Authenticating..."    
    ProgressWheel:textColor="#222"   
    ProgressWheel:textSize="14sp"   
    ProgressWheel:rimColor="#330097D6"   
    ProgressWheel:barLength="60dp"    
    ProgressWheel:barColor="#0097D6"   
    ProgressWheel:barWidth="5dp"   
    ProgressWheel:rimWidth="2dp" /> 
```
	
**Java:**   
First you need to either get a ProgressWheel from a layout file, or initalise one. Do this by:

-  `ProgressWheel pw = new ProgressWheel(myContext, myAttributes);`
-  `ProgressWheel pw = (ProgressWheel) findViewById(R.id.pw_spinner);`

To spin the progress wheel, you just call .`spin()` and to stop it spinning, you call `.stopSpinning()`

Incrementing the progress wheel is slightly more tricky, you call `.incrementProgress()`. However, this is out of 360,  
(because a circle has 360 degrees), and will automatically reset once you get past 360. A percentage display is   
automatically displayed.

Using as a library project
--------------------------

To use it as a library in Android Studio, please edit build.gradle.

Modify:

    apply plugin: 'android'

Into:

    apply plugin: 'android-library'

Since Android SDK Tools revision 17 (released March 2012), this component can
be used as a library project. In this case, you do *not* need to copy anything
into your project's attrs.xml, and you must use the following namespace URI,
instead of the above:

`xmlns:ProgressWheel="http://schemas.android.com/apk/res-auto"`

Otherwise, usage should be the same.


Todd Davies - 2012
