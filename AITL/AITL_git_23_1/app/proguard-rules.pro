# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/nteam/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-dontwarn okio.**
#-keep public class org.apache.http
#-keepclassmembers class org.apache.http {
#   public *;
##}
#-renamesourcefileattribute SourceFile
#-keepattributes  Signature,SourceFile,LineNumberTable
#-keepattributes Signature
#-keep public class * extends android.app.Application

-dontwarn com.google.**
-keep class com.google.** { *; }

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

-keep class javax.xml.bind.** { *; }
-dontwarn javax.xml.bind.**

-keep class android.os.Handler.** { *; }

-keep class org.mortbay.** { *; }
-dontwarn org.mortbay.**

-keep class sun.misc.Unsafe.** { *; }
-dontwarn sun.misc.Unsafe.**

-keep class rx.internal.util.unsafe.** { *; }
-dontwarn rx.internal.util.unsafe.**

# Package location
-keep class com.mapbox.mapboxsdk.location.** { *; }
-dontwarn com.mapbox.mapboxsdk.location.**
-keep class org.chromium.** { *; }
-keepattributes

-dontwarn com.viewpagerindicator.**
-ignorewarnings
-keep class * {
    public private *;
}

-keepattributes *Annotation*
-keep class android.support.v7.widget.SearchView { *; }


-keep class com.estimote.sdk.** { *; }
-keep interface com.estimote.sdk.** { *; }
-dontwarn com.estimote.sdk.**

#Crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keepattributes Exceptions, Signature, LineNumberTable
-keep public class * extends java.lang.Exception
-printmapping mapping.txt


#-dontwarn android.support.v4.**

# camerakit
-dontwarn com.google.android.gms.**

# EventBus Data
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }