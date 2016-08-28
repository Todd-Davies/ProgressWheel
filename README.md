Progress Wheel
=============

This is a custom component for Android intended for use instead of a progress bar.

![Sample Image](https://github.com/Todd-Davies/ProgressWheel/raw/master/sample_image.png "An example implementation")
![Sample Image 3](https://github.com/Todd-Davies/ProgressWheel/raw/master/sample_image_3.png "Another example implementation")
![Sample Image 4](https://github.com/Todd-Davies/ProgressWheel/raw/master/sample_image_4.png "Another example implementation")

Compare it side by side with the Android 2x progress wheel:

![Sample Image 5](https://github.com/Todd-Davies/ProgressWheel/raw/master/sample_image5.png "Side by side comparison")

A complete walkthrough of how to use this component in your app
-------------

**XML:**   
To implement the view in your xml layout do the following:

1. Add the following to your attrs.xml file (in res/values):
``` xml
<declare-styleable name="ProgressWheel">   
	<attr name="pwText" format="string" />   
	<attr name="pwTextColor" format="color" />   
	<attr name="pwTextSize" format="dimension" />   
	<attr name="pwBarColor" format="color" />   
	<attr name="pwRimColor" format="color" />   
	<attr name="pwRimWidth" format="dimension" />   
	<attr name="pwSpinSpeed" format="integer" />     
	<attr name="pwCircleColor" format="color" />     
	<attr name="pwRadius" format="dimension" />   
	<attr name="pwBarWidth" format="dimension" />   
	<attr name="pwBarLength" format="dimension" />
	<attr name="pwDelayMillis" format="dimension"/>
	<attr name="pwContourColor" format="color"/>
	<attr name="pwContourSize" format="float"/>
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
    ProgressWheel:pwText="Authenticating..."    
    ProgressWheel:pwTextColor="#222"   
    ProgressWheel:pwTextSize="14sp"   
    ProgressWheel:pwRimColor="#330097D6"   
    ProgressWheel:pwBarLength="60dp"    
    ProgressWheel:pwBarColor="#0097D6"   
    ProgressWheel:pwBarWidth="5dp"   
    ProgressWheel:pwRimWidth="2dp" /> 
```
	
**Java:**   
First you need to either get a ProgressWheel from a layout file, or initalise one. Do this by:

-  `ProgressWheel pw = new ProgressWheel(myContext, myAttributes);`
-  `ProgressWheel pw = (ProgressWheel) findViewById(R.id.pw_spinner);`

To spin the progress wheel, you just call .`startSpinning()` and to stop it spinning, you call `.stopSpinning()`

Incrementing the progress wheel is slightly more tricky, you call `.incrementProgress()`. However, this is out of 360,  
(because a circle has 360 degrees), and will automatically reset once you get past 360. A percentage display is   
automatically displayed.

Using as a dependency
--------------------------

Add this to your build.gradle:

```gradle
	repositories {
	    maven { url "https://jitpack.io" }
	}
	
	dependencies {
	    compile 'com.github.Todd-Davies:ProgressWheel:1.2'
	}
```

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


[Todd Davies](http://todddavies.co.uk) - [@Todd__Davies](http://twitter.com/todd__davies) - 2012
