SUMMARY = "Chromium GPU library"
DESCRIPTION = "The code for the Chromium GPU process lives there; responsible \
for detecting the GPU hardware OpenGL capabilities, initializing the matching \
renderer and starting the dedicated Zygote process."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-crypto chromium-gpu-commandbuffer chromium-ipc chromium-ui-gfx chromium-ui-gl protobuf protobuf-native python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "6fb6073a089ecc32dc2827181983709778e7838e"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRCREV_angle = "457f1d929e5fd911e0ad9ea53db43010dfae0662"
SRCREV_smhasher = "e87738e57558e0ec472b2fc3a643b838e5b6e88f"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           git://chromium.googlesource.com/angle/angle.git;protocol=https;name=angle;destsuffix=git/third_party/angle \
           git://chromium.googlesource.com/external/smhasher.git;protocol=https;name=smhasher;destsuffix=git/third_party/smhasher/src \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/${NAME}"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'x11', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE"
PACKAGECONFIG[x11] = "-DBACKEND=X11,,virtual/libx11 libxext"

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu -I${STAGING_INCDIR}/chromium/third_party/mesa/src/include -DGL_CONTEXT_LOST_KHR=0x0507"
# we NEED to include "third_party/mesa/src/include", because it contains
# GL headers whose definitions match those from "third_party/khronos/KHR/*.h",
# which are heavily used by ANGLE
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lcrcrypto -lgpu_command_buffer -lipc -lui_gfx -lui_gl"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       cd ${S}/../third_party/angle
       mkdir -p ${D}${includedir}/chromium/third_party/angle
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/angle
       # we need to copy generated headers living in the "build" directory
       cd ${B}/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
