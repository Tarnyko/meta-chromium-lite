SUMMARY = "Chromium GPU Command Buffer"
DESCRIPTION = "Stores, validates, and error-checks OpenGL ES commands before \
they are sent to the GPU."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV = "0e3186cd4345cd9cb4b9b9958272044ca118a261"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/gpu/command_buffer"

inherit cmake pkgconfig

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/gpu/command_buffer
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/gpu/command_buffer
       cp ../gpu_export.h ${D}${includedir}/chromium/gpu
       cd ${S}/../GLES2
       mkdir -p ${D}${includedir}/chromium/GLES2
       mkdir -p ${D}${includedir}/chromium/gpu/GLES2
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/GLES2
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/gpu/GLES2
       cd ${S}/../../third_party/khronos
       mkdir -p ${D}${includedir}/chromium/third_party/khronos
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/khronos
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
