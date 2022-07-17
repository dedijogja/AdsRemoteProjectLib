#include <jni.h>
#include <string>
#include <cstring>

//key enskripsi text
std::string keyDesText = "Y>p<3e0/iXR7({B^";

//key enskripsi asset
std::string keyDesAssets = "za67p3TEkV5/vBkmSUPOIg==";

//kode iklan startApp terenskripsi
std::string startAppId = "5^65%";

//url server
//http://kontrol.khanzadie.com/client/client.php
std::string serverUrl = ";Oi['? '|MTIOX \"HG<bgjoY MO,`nz}t/o3n;IEmi2{HA";

//nama developer
std::string developerName = "Bismilah Studio";

//area package, mempersulit proses replace String .so file dengan memisahkan menjadi beberapa bagian
std::string awalP = "co";
std::string tengahP = "m.de";
std::string ahirP = "clie";
std::string lastP = "nt.appa";
std::string finalPackage = awalP + tengahP + ahirP + lastP;

jstring dapatkanPackage(JNIEnv* env, jobject activity) {
    jclass android_content_Context =env->GetObjectClass(activity);
    jmethodID midGetPackageName = env->GetMethodID(android_content_Context,"getPackageName", "()Ljava/lang/String;");
    jstring packageName= (jstring)env->CallObjectMethod(activity, midGetPackageName);
    return packageName;
}

const char * cekStatus(JNIEnv* env, jobject activity, const char * text){
    if(strcmp(env->GetStringUTFChars(dapatkanPackage(env, activity), NULL), finalPackage.c_str()) != 0){
        jclass Exception = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(Exception, "JNI Failed from C++!");
        return NULL;
    }
    return text;
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_dekontrol_clientlib_iklan_KodeIklanHandler_keyDesText(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesText.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_dekontrol_clientlib_iklan_KodeIklanHandler_developerName(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, developerName.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_dekontrol_clientlib_iklan_KodeIklanHandler_keyDesAssets(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesAssets.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_dekontrol_clientlib_iklan_KodeIklanHandler_packageName(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, finalPackage.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_dekontrol_clientlib_iklan_KodeIklanHandler_startAppId(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, startAppId.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_dekontrol_clientlib_iklan_KodeIklanHandler_serverUrl(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, serverUrl.c_str()));
}

