SUMMARY = "Chromium UI Events helper library - Blink additions"
DESCRIPTION = "Blink web engine additions for the UI Events library."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-ui-gfx chromium-ui-events chromium-cc chromium-blink"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "ef69e7c04fb0d490382100162c925ac7458d3188"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/events/blink"

inherit cmake pkgconfig

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lui_gfx -lui_events -lcc -lblink"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/events/blink
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/events/blink
       cd ${S}/../gestures/blink
       mkdir -p ${D}${includedir}/chromium/ui/events/gestures/blink
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/events/gestures/blink
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
