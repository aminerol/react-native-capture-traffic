
# react-native-capture-traffic

## Getting started

`$ npm install react-native-capture-traffic --save`

### Mostly automatic installation

`$ react-native link react-native-capture-traffic`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-capture-traffic` and add `RNCaptureTraffic.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNCaptureTraffic.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactnative.capturetraffic.RNCaptureTrafficPackage;` to the imports at the top of the file
  - Add `new RNCaptureTrafficPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-capture-traffic'
  	project(':react-native-capture-traffic').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-capture-traffic/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-capture-traffic')
  	```


## Usage
```javascript
import RNCaptureTraffic from 'react-native-capture-traffic';

// TODO: What to do with the module?
RNCaptureTraffic;
```
  