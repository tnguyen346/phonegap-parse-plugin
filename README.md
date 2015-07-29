Phonegap Parse.com Plugin
=========================

Phonegap/Cordova 3.0+ plugin for Parse.com push service. This plugin uses a modified version of Parse Android SDK to prevent the required Application class, and making it easier to use on Phonegap.

Using [Parse.com's](http://parse.com) REST API for push requires the installation id, which isn't available in JS

Features
--------
* Initialise Parse from JS
* Add, Remove and List subscriptions to channels
* Build on Phonegap Builder

Installation
------------

To install plugin locally use:

```
phonegap local plugin add https://github.com/ropilz/phonegap-parse-plugin --variable APP_ID=PARSE_APP_ID --variable CLIENT_KEY=PARSE_CLIENT_KEY
cordova plugin add https://github.com/ropilz/phonegap-parse-plugin --variable APP_ID=PARSE_APP_ID --variable CLIENT_KEY=PARSE_CLIENT_KEY
```

To use plugin on Phonegap Builder use:
```xml
<gap:plugin name="com.medlei.pushplugin" version="0.1.1" >
        <param name="APP_ID" value="PARSE_APP_ID" />
        <param name="CLIENT_KEY" value="PARSE_CLIENT_KEY" />
</gap:plugin>
```

Initial Setup
-------------

A parsePlugin variable is defined globally (e.g. $window.parsePlugin).

Once the device is ready (see: http://docs.phonegap.com/en/4.0.0/cordova_events_events.md.html#deviceready), call ```parsePlugin.initialize()```. This will register the device with Parse, you should see this reflected in your Parse control panel. After this runs you probably want to save the installationID somewhere, and perhaps subscribe the user to a few channels. Here is a contrived example.

(Note: When using Windows Phone, clientKey must be your .NET client key from Parse. So you will need to set this based on platform i.e. if( window.device.platform == "Win32NT"))

```
parsePlugin.initialize(appId, clientKey, function() {

	parsePlugin.subscribe('SampleChannel', function() {

		parsePlugin.getInstallationId(function(id) {

			/**
			 * Now you can construct an object and save it to your own services, or Parse, and correlate users to parse installations
			 *
			 var install_data = {
			  	installation_id: id,
			  	channels: ['SampleChannel']
			 }
			 *
			 */

		}, function(e) {
			alert('error');
		});

	}, function(e) {
		alert('error');
	});

}, function(e) {
	alert('error');
});

```

Alternatively, we can store the user in the installation table and use queries to push notifications.

```
// on sign in, add the user pointer to the Installation
parsePlugin.initialize(appId, clientKey, function() {

  parsePlugin.getInstallationObjectId( function(id) {
    // Success! You can now use Parse REST API to modify the Installation
    // see: https://parse.com/docs/rest/guide#objects for more info
    console.log("installation object id: " + id)
  }, function(error) {
    console.error('Error getting installation object id. ' + error);
  });

}, function(e) {
	alert('Error initializing.');
});

```

To receive notification callbacks, on device ready:


```
parsePlugin.registerCallback('onNotification', function() {

  window.onNotification = function(pnObj) {
    alert('We received this push notification: ' + JSON.stringify(pnObj));
    if (pnObj.receivedInForeground === false) {
    	// TODO: route the user to the uri in pnObj
    }
  };

}, function(error) {
  console.error(error);
});

```

Usage
-----
```
<script type="text/javascript">
	parsePlugin.initialize(appId, clientKey, function() {
		alert('success');
	}, function(e) {
		alert('error');
	});

	parsePlugin.getInstallationId(function(id) {
		alert(id);
	}, function(e) {
		alert('error');
	});

	parsePlugin.getSubscriptions(function(subscriptions) {
		alert(subscriptions);
	}, function(e) {
		alert('error');
	});

	parsePlugin.subscribe('SampleChannel', function() {
		alert('OK');
	}, function(e) {
		alert('error');
	});

	parsePlugin.unsubscribe('SampleChannel', function(msg) {
		alert('OK');
	}, function(e) {
		alert('error');
	});

	parsePlugin.resetBadge(function() {
    alert('OK');
  }, function(e) {
    alert('error');
  });

	parsePlugin.trackEvent(function(name, dimensions) {
    alert('OK');
  }, function(e) {
    alert('error');
  });
</script>
```

Quirks
------

### Android

Parse needs to be initialized once in the `onCreate` method of your application class using the `initializeParseWithApplication` method.

If you don’t have an application class (which is most likely the case for a Cordova app), you can create one using this template:

```java
package my.package.namespace;

import android.app.Application;
import org.apache.cordova.core.ParsePlugin;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParsePlugin.initializeParseWithApplication(this);
    }

}
```

And add your application name to `AndroidManifest.xml`:

```xml
<application android:name="my.package.namespace.App" ... >...</application>
```


Compatibility
-------------
Phonegap/Cordova > 3.0
