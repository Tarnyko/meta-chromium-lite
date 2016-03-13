SUMMARY = "Chromium UI Touch Selection helper library"
DESCRIPTION = "Handles touch input events, on top of dedicated UI Events \
library and Aura."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-ui-gfx chromium-ui-events chromium-ui-base chromium-ui-aura chromium-ui-resources"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "96c41eccee47e17a313bd68606f586e212cb840e"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/touch_selection"

inherit cmake pkgconfig

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lui_gfx -lui_events -lui_base -lui_aura -lui_resources"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/touch_selection
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/touch_selection
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
