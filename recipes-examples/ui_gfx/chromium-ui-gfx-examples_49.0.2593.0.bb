SUMMARY = "Chromium UI Gfx examples"
DESCRIPTION = "Some examples files for the Chromium UI Gfx helper library."
HOMEPAGE = "http://www.iot.bzh"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-ui-gfx"

SRC_URI = " \
           file://LICENSE \
           file://CMakeLists.txt \
           file://chromium_ui_gfx-1_color.cc \
           file://chromium_ui_gfx-2_text.cc \
          "

inherit cmake

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -DUSE_AURA"
CXXFLAGS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', ' -DUSE_OZONE', ' -DUSE_X11', d)}"
LDFLAGS_append = " -Wl,-rpath-link,${STAGING_LIBDIR}/chromium -Wl,-rpath,${libdir}/chromium"
LDFLAGS_remove = " -Wl,--as-needed"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lui_gfx -lskia -lbase'"


do_configure_prepend() {
       mkdir -p ${S}
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
       cp ${WORKDIR}/*.cc ${S}
}

FILES_${PN}-dbg += "${bindir}/chromium/.debug/*"
