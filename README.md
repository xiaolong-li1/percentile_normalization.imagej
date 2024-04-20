# Percentile_normalization Plugin for imagej

## Introduction

*This Imagej plugin provides a real-time visualization of the Image Percentile Normalization result with Boundary parameters that can be adjusted easily.*<br><br>

## How to use

### 1. Add the [Percentile_Norm-0.4.2-SNAPSHOT.jar](target/Percentile_Norm-0.4.2-SNAPSHOT.jar)to your imageJ plugin folder as usual and it will show up in `process->Percentile_Normalization`:
<div align="center">

<img src="./imgs/image-1.png" height="330" width="190" >
 </div>
<!-- ![alt text](./imgs/image-1.png#pic_center) -->
<br>

### 2. The mainboard and brief introduction to some confusing concepts in it
<br>

![alt text](./imgs/UI.png#pic_center)
* ###  the two number(0,255 in this snapshot) under the histogram:
The left one represent the smallest pixel value in the scope(a slice or stack depending on the *Mode* setting).Accordingly the right one stands for the biggest value in the scope.
* ### Mode:     
this configuration is to specify the scope where we get the x% rank. So the stack scope and slice scope is pretty easy to understand now.
+ ### Set: 
The same function as the sliders above.Just another way the get the input argument lower_percentile and the upper_percentile.
* ### Reset
Undo the changes that happens after the last apply.
* ### Auto
Just click this buttom, the appropriate manipulation may happens on your image. 
* ### Apply
Only do the apply can we save it in the disk later.
## Here comes a presentation.
![alt text](./imgs/Video_2024-04-20_230501.gif)
