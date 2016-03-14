SUMMARY = "Chromium Skia library"
DESCRIPTION = "Skia is a low-level drawing API used by both the Blink web \
engine and the Chromium UI Gfx library to draw the Chromium UI. It handles \
canvases, colors, animations... font rendering, on the other hand, is left to \
the platform (specifically Freetype/Fontconfig on GNU/Linux). The Chromium \
version differs from the standalone one, in that it overrides a few classes \
(to expose private elements) and adds several specific ones."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-mojo libjpeg-turbo libpng12 giflib libwebp freetype fontconfig python-native python-jinja2-native python-ply-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "1464222d7ee83e93467b0d91399579bed5b57de2"
SRCREV_skia-std = "19b0420e877d3e27d5315d8a539e671cf2b479eb"
SRCREV_mojo = "cf59faa001ce1dc4ed51e86d1069346152db6cb4"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://chromium.googlesource.com/skia.git;protocol=https;name=skia-std;destsuffix=git/third_party/skia \
           git://github.com/Tarnyko/chromium-mojo.git;name=mojo;destsuffix=git/mojo \
           file://LICENSE \
           file://CMakeLists.txt \
           file://Toolchain-arm.cmake \
           file://Toolchain-x86.cmake \
           file://Toolchain-x86_64.cmake \
          "

S = "${WORKDIR}/git/third_party/skia"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'x11', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=EGL,,virtual/egl virtual/libgles2"
PACKAGECONFIG[x11] = "-DBACKEND=GLX,,virtual/libx11 virtual/libgl libglu"

EXTRA_OECMAKE_append_armv5 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-arm.cmake"
EXTRA_OECMAKE_append_armv6 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-arm.cmake"
EXTRA_OECMAKE_append_i586 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86.cmake"
EXTRA_OECMAKE_append_i686 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86.cmake"
EXTRA_OECMAKE_append_x86_64 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86_64.cmake"

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/mojo"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lmojo"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/third_party/skia
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/skia
       cd ${S}/../../skia
       mkdir -p ${D}${includedir}/chromium/skia
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/skia
       # this is required to build Content
       mkdir -p ${D}${datadir}/chromium/skia
       cp --parents `find . -name "*.mojom"` ${D}${datadir}/chromium/skia
       # we need to copy generated headers living in the "build" directory
       cd ${B}/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dev += "${datadir}/chromium/*"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
