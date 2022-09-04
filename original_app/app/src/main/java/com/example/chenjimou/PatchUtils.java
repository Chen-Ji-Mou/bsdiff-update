package com.example.chenjimou;

public class PatchUtils
{
    static {
        System.loadLibrary("bspatch_utils");
    }

    public static native int patch(String oldApkPath, String newApkPath, String patchFilePath);
}