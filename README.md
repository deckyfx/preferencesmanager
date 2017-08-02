# preferencesmanager
[![](https://jitpack.io/v/deckyfx/preferencesmanager.svg)](https://jitpack.io/#httprequest/preferencesmanager)

Manage Preferences storage acros multiple xml files

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
	repositories {
	...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency

```gradle
dependencies {
    compile 'com.github.deckyfx:preferencesmanager:0.5'
}
```

## Sample Code


Init the class and configure it
```java
...

PreferencesManager Manager = new PreferencesManager(App.MAIN_CONTEXT);
Manager.getDefault().loadDefaultValue(R.xml.default_preferences);
Manager.getDefault().clear();
Manager.getDefault().set(/* Key */, /* Value */);
Manager.getDefault().get(/* Key */);
...

```

Then init DBSession
```java
...
HashMap<String, Object> params = new HashMap<String, Object>();
HashMap<String, Object> headers = new HashMap<String, Object>();
this.G.HTTP_CLIENT.send(/* context */, /* path or full url */, /* Method */, params, headers, /* request ID */, /* callback */);
...

```

More sample is [here]

## Feature:

 * 