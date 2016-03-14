SUMMARY = "Chromium Compositor library"
DESCRIPTION = "Composites 2 rendering sources: the Blink web engine, and the \
Chromium UI around it (issued by UI Views/Gfx). It is needed because although \
these 2 sources both use Skia, they are separated for design and security \
reasons, and we need a way to merge them in the final browser window."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-skia chromium-ui-gfx chromium-ui-events chromium-ui-gl chromium-gpu chromium-media protobuf protobuf-native python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "e0ec441a24ba8d78e0e69b642066c174c50abbfe"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           file://LICENSE \
           file://CMakeLists.txt \
           file://Toolchain-arm.cmake \
           file://Toolchain-x86.cmake \
           file://Toolchain-x86_64.cmake \
          "

S = "${WORKDIR}/git/${NAME}"

inherit cmake pkgconfig

EXTRA_OECMAKE_append_armv5 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-arm.cmake"
EXTRA_OECMAKE_append_armv6 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-arm.cmake"
EXTRA_OECMAKE_append_i586 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86.cmake"
EXTRA_OECMAKE_append_i686 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86.cmake"
EXTRA_OECMAKE_append_x86_64 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86_64.cmake"

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lskia -lui_gfx -lui_events -lmedia -lui_gl -lgpu"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       # we need to copy generated headers living in the "build" directory
       cd ${B}/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
