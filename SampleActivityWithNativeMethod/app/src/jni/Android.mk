LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := sample
LOCAL_SRC_FILES := sample.c
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
