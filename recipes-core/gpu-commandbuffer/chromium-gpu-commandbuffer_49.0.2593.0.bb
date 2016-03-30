SUMMARY = "Chromium GPU Command Buffer"
DESCRIPTION = "Stores, validates, and error-checks OpenGL ES commands before \
they are sent to the GPU."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV = "1285fc8eebc70de99d1fb4f61ac3343a7b81fd72"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git \
           file://LICENSE \
           file://CMakeLists.txt \
           file://use_chromium_gl2_header.patch \
          "

S = "${WORKDIR}/git/gpu/command_buffer"

inherit cmake pkgconfig

FULL_OPTIMIZATION = ""
CXXFLAGS_remove = "-fvisibility-inlines-hidden"
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lbase'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
       # we apply this patch separately because it is outside of the source tree
       cd ${S}/../..
       patch -f -p1 < use_chromium_gles_headers.patch || true
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
