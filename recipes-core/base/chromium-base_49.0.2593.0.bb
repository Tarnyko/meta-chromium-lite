SUMMARY = "General-purpose Chromium toolkit"
DESCRIPTION = "Handles logging, callbacks, string and numeral conversions, \
command line arguments, file and folder operations, message loops, threads, \
time and date..."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "libevent icu python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "dcd1e3f4218b8a119fef068c2993a7989fde8922"
SRCREV_build = "1aa26aaba531135f3ca9ffd522bffb3f7b8f1be6"
SRCREV_gtest = "6f8a66431cb592dad629028a50b3dd418a408c87"
SRCREV_icu = "8d342a405be5ae8aacb1e16f0bc31c3a4fbf26a2"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-build.git;name=build;destsuffix=git/build \
           git://github.com/google/googletest.git;name=gtest;destsuffix=git/testing/gtest \
           git://chromium.googlesource.com/chromium/deps/icu.git;protocol=https;name=icu;destsuffix=git/third_party/icu \
           file://LICENSE \
           file://CMakeLists.txt \
           file://time_formatting_noicufork.patch \
          "

S = "${WORKDIR}/git/${NAME}"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE,-DBACKEND=X11"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       # we need to provide these headers, too
       cd ${S}/../build
       mkdir -p ${D}${includedir}/chromium/build
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/build
       cd ${S}/../testing
       mkdir -p ${D}${includedir}/chromium/testing
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/testing
       cd ${S}/../third_party/icu
       mkdir -p ${D}${includedir}/chromium/third_party/icu
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/icu
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
