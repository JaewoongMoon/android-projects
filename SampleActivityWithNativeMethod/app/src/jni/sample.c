//
// Created by jwmoon on 2016-12-14.
//

#include <jni.h>
#include <android/log.h>

#define LOGINFO(x...) _android_log_print(ANDROID_LOG_INFO, "SampleJNI", x)

jstring Java_com_oreilly_demo_pa_ch18_SampleActivityWithNativeMethods_whatAmI(
        JNIEnv* env, jobject thisobject) {
    LOGINFO("SampleJNI", "Sample Info Log Output");

    return (*env)->NewStringUTF(env, "Unknown");
}

