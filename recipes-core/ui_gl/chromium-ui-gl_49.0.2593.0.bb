SUMMARY = "Chromium UI GL helper library"
DESCRIPTION = "Provides various OpenGL utility functions."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-gpu-commandbuffer chromium-skia chromium-ui-gfx chromium-ui-base virtual/libgles2"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "09152ebaec144fb4f96d2e98d1c9ebc4b48b834a"
SRCREV_mesa = "ef811c6bd4de74e13e7035ca882cc77f85793fef"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://chromium.googlesource.com/chromium/deps/mesa.git;protocol=https;name=mesa;destsuffix=git/third_party/mesa/src \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/gl"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'x11', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE,,chromium-ozone"
PACKAGECONFIG[x11] = "-DBACKEND=X11,,virtual/libx11 libxcomposite libxext"

CXXFLAGS_remove = "-fvisibility-inlines-hidden"
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/third_party/khronos -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu -fpermissive -DGL_CONTEXT_LOST_KHR=0x0507"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', ' -lozone', '', d)} -lui_gfx -lskia -lgpu_command_buffer -lbase'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/gl
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/gl
       # these specific Mesa headers are required by GPU
       cd ${S}/../../third_party/mesa
       mkdir -p ${D}${includedir}/chromium/third_party/mesa
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/mesa
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
