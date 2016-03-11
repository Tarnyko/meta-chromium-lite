SUMMARY = "Chromium Ozone library"
DESCRIPTION = "Ozone is the new platform UI abstraction layer for Chromium, \
designed so that various platforms can all subclass Ozone. This contains \
only the Ozone-Wayland implementation and the necessary Ozone generic classes \
provided by Chromium (we do not build other implementations such as Ozone-DRM)."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-ipc chromium-skia chromium-ui-gfx chromium-ui-base wayland virtual/egl libxkbcommon python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "610d0b9961230f3d9fbc2e764b3e263ec66ef9d1"
SRCREV_ozone-wayland = "9816917277df5defa2d2787aeea3c857465714dc"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/01org/ozone-wayland.git;name=ozone-wayland;destsuffix=git/ozone \
           file://LICENSE \
           file://CMakeLists.txt \
           file://windowmanager_noauradeps.patch \
          "

S = "${WORKDIR}/git/ozone"

inherit cmake pkgconfig

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lipc -lskia -lui_gfx -lui_base"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
       # we apply these patches separately because they live in the source tree
       cd ${S}/..
       patch -f -p1 < ozone/patches/0007-Add-needed-support-in-PlatformWindow.patch
       patch -f -p1 < ozone/patches/0013-Add-drag-and-drop-interfaces-to-PlatformWindowDelega.patch
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ozone
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ozone
       cd ${S}/../ui/ozone
       mkdir -p ${D}${includedir}/chromium/ui/ozone
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/ozone
       cd ${S}/../ui/display
       mkdir -p ${D}${includedir}/chromium/ui/display
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/display
       # we need to copy generated headers living in the "build" directory
       cd ${B}/ui/ozone
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/ozone
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
