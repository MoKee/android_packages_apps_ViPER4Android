#
# Copyright (C) 2016 The Vipers Audio Open Source Project
# Copyright (C) 2016 The MoKee Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := \
    $(call all-java-files-under, src) \
    $(call all-java-files-under, libs/RootShell/src) \
    $(call all-java-files-under, libs/RootTools/src) \
    $(call all-java-files-under, libs/swipebacklibrary/src)

LOCAL_STATIC_JAVA_LIBRARIES += \
    android-support-annotations \
    android-support-design \
    android-support-v4 \
    android-support-v7-recyclerview \
    android-support-v7-appcompat \
    android-support-v13

LOCAL_RESOURCE_DIR := \
    frameworks/support/design/res \
    frameworks/support/v7/appcompat/res \
    frameworks/support/v7/recyclerview/res \
    $(LOCAL_PATH)/libs/swipebacklibrary/res \
    $(LOCAL_PATH)/res

LOCAL_AAPT_FLAGS := --auto-add-overlay \
    --extra-packages android.support.design:android.support.v7.appcompat:android.support.v7.recyclerview:me.imid.swipebacklayout.lib

LOCAL_PROGUARD_FLAGS := -ignorewarnings -include build/core/proguard_basic_keeps.flags

LOCAL_PROGUARD_ENABLED := nosystem

LOCAL_PROGUARD_FLAG_FILES := proguard.flags

LOCAL_JACK_ENABLED := disabled

LOCAL_PACKAGE_NAME := ViPER4Android

LOCAL_CERTIFICATE := platform

LOCAL_PRIVILEGED_MODULE := true

include $(BUILD_PACKAGE)
