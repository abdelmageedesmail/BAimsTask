#include <jni.h>
#include <string>


extern "C"
JNIEXPORT jstring

JNICALL
Java_com_abdelmageed_baimstask_data_di_KtorModule_getBaseUrl(JNIEnv *env, jobject thiz) {
    std::string baseURL = "https://api.openweathermap.org/data/2.5/forecast?";
    return env->NewStringUTF(baseURL.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_abdelmageed_baimstask_data_di_RepositoryModule_getApiKey(JNIEnv *env, jobject thiz) {
    std::string baseURL = "96d3908dd725b540dc67aa8d6d4e9bcd";
    return env->NewStringUTF(baseURL.c_str());
}