//
// Created by chenjimou on 2022/9/3.
//

#include <jni.h>
#include <android/log.h>
#include "bsdiff/bspatch.c"

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_chenjimou_PatchUtils_patch(JNIEnv *env, jclass clazz,
                                            jstring old_apk_path, jstring new_apk_path,
                                            jstring patch_file_path)
{
    int args = 4;
    char * argv[args];

    argv[0] = "bspatch";
    argv[1] = (char *) (env -> GetStringUTFChars(old_apk_path, nullptr));
    argv[2] = (char *) (env -> GetStringUTFChars(new_apk_path, nullptr));
    argv[3] = (char *) (env -> GetStringUTFChars(patch_file_path, nullptr));

    __android_log_print(ANDROID_LOG_DEBUG,"[JNI] PatchUtils.patch",
                        "start old_apk_path: %s new_apk_path: %s patch_file_path: %s", argv[1], argv[2], argv[3]);

    int result = execute_patch(args, argv);

    __android_log_print(ANDROID_LOG_DEBUG,"[JNI] PatchUtils.patch","finish result: %d", result);

    env -> ReleaseStringUTFChars(old_apk_path, argv[1]);
    env -> ReleaseStringUTFChars(new_apk_path, argv[2]);
    env -> ReleaseStringUTFChars(patch_file_path, argv[3]);

    return result;
}