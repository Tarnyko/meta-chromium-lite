SUMMARY = "Chromium Content Shell"
DESCRIPTION = "Content Shell is a minimal browser designed for testing purposes; normally able \
to execute a series of layout tests, we disable this features to avoid dependencies on the \
GTest/GMock framework. It uses Views to display an address bar, Back/Previous/Refresh buttons, \
and a right-click contextual menu."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-v8 chromium-net chromium-mojo chromium-skia chromium-ui-accessibility chromium-blink chromium-ui-aura chromium-content chromium-ui-views python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = ""
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           file://LICENSE \
           file://CMakeLists.txt \
           file://disable_tests_breakpad.patch \
          "

S = "${WORKDIR}/git/content/shell"

inherit cmake pkgconfig

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lv8 -lnet -lmojo -lskia -lui_accessibility -lblink -lui_aura -lcontent -lui_views"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/content/shell
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/content/shell
       # we need to copy generated headers living in the "build" directory
       cd ${B}/content/shell
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/content/shell
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${datadir}/chromium
       cp ${B}/content/shell/resources/*.pak ${D}${datadir}/chromium
}

FILES_${PN} += "${libdir}/chromium/*.so ${datadir}/chromium/*.pak"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
