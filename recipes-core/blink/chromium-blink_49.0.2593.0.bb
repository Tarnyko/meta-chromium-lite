SUMMARY = "Chromium Blink library"
DESCRIPTION = "Blink is the Chromium web engine; originally a fork of WebKit, \
it parses HTML webpages, handles DOM operations, passes JavaScript code to V8 \
via build-generated IDL bindings, and do its own rendering using Skia."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-v8 chromium-url chromium-gin chromium-skia chromium-ui-gfx chromium-gpu chromium-cc icu harfbuzz libwebp libpng12 libxml2 libxslt sqlite3 bison-native gperf-native python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "e86321bd06ff0d4d8a3418c8f482af2e9e3d9b9b"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           file://LICENSE \
           file://CMakeLists.txt \
           file://Toolchain-arm.cmake \
           file://Toolchain-x86.cmake \
           file://Toolchain-x86_64.cmake \
           file://core_with_deps.lst \
           file://fix_generated_bindings.sh \
           file://mediarecorder_fix_event.patch \
          "

S = "${WORKDIR}/git/third_party/WebKit/Source"

inherit cmake pkgconfig

DEPENDS_append_i586 = " yasm-native"
DEPENDS_append_i686 = " yasm-native"
DEPENDS_append_x86_64 = " yasm-native"
EXTRA_OECMAKE_append_arm = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-arm.cmake"
EXTRA_OECMAKE_append_i586 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86.cmake"
EXTRA_OECMAKE_append_i686 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86.cmake"
EXTRA_OECMAKE_append_x86_64 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86_64.cmake"

CXXFLAGS_remove = "-fvisibility-inlines-hidden"
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/v8/include -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu -I${STAGING_INCDIR}/chromium/third_party/skia/include/ports -I${STAGING_INCDIR}/chromium/third_party/skia/include/effects -I${STAGING_INCDIR}/chromium/third_party/khronos"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lcc -lgpu -lui_gfx -lskia -lgin -lurl_lib -lv8 -lbase'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
       cp ${WORKDIR}/core_with_deps.lst ${S}
       cp ${WORKDIR}/fix_generated_bindings.sh ${S}
}

do_install_append() {
       cd ${S}/..
       mkdir -p ${D}${includedir}/chromium/third_party/WebKit
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/WebKit
       # we need to copy generated headers living in the "build" directory
       cd ${B}/${NAME}
       mkdir -p ${D}${includedir}/chromium/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       cd ${B}/bindings
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/WebKit/Source/bindings
       cd ${B}/core
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/WebKit/Source/core
       cd ${B}/modules
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/WebKit/Source/modules
       cd ${B}/platform
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/WebKit/Source/platform
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${datadir}/chromium
       cp ${B}/${NAME}/public/resources/*.pak ${D}${datadir}/chromium
       # this is required to build Content
       cp -r ${B}/devtools ${D}${datadir}/chromium
       cd ${S}/devtools
       cp --parents `find . -name "*.json" -o -name "*.js" -o -name "*.css" -o -name "*.html"` ${D}${datadir}/chromium/devtools
}

FILES_${PN} += "${libdir}/chromium/*.so ${datadir}/chromium/*.pak"
FILES_${PN}-dev += "${datadir}/chromium/devtools/*"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
