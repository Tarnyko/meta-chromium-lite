SUMMARY = "V8 JavaScript engine"
DESCRIPTION = "V8 parses, interprets and JIT-compiles JavaScript code to C. \
It is heavily used by the Chromium Blink web engine, which has V8 bindings \
to pass and retrieve data to and from V8."
HOMEPAGE = "https://developers.google.com/v8"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=71f2c485344c921f659ea9f7d895ea22"

DEPENDS = "icu python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV = "8311bed8649e1195de0ddeba71f876143c9a7f95"
SRC_URI = " \
           git://chromium.googlesource.com/v8/v8.git;protocol=https;branch=4.9.211 \
           file://CMakeLists.txt \
           file://Toolchain-arm.cmake \
           file://Toolchain-x86.cmake \
           file://Toolchain-x86_64.cmake \
          "

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE_append_armv5 = "-DCMAKE_TOOLCHAIN_FILE=../Toolchain-arm.cmake"
EXTRA_OECMAKE_append_armv6 = "-DCMAKE_TOOLCHAIN_FILE=../Toolchain-arm.cmake"
EXTRA_OECMAKE_append_i586 = "-DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86.cmake"
EXTRA_OECMAKE_append_i686 = "-DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86.cmake"
EXTRA_OECMAKE_append_x86_64 = "-DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86_64.cmake"

do_configure_prepend() {
       cp ${WORKDIR}/CMakeLists.txt ${S}
       cp ${WORKDIR}/Toolchain-*.cmake ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
