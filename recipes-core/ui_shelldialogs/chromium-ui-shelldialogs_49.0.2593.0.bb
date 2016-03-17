SUMMARY = "Chromium UI Shell Dialogs helper library"
DESCRIPTION = "Provides platform-specific dialogs windows (file selector...)."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-skia chromium-ui-base chromium-ui-aura chromium-ui-strings"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "1dee6a95ca3400df7f11a4bab363bb5f8fac8e0b"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/shell_dialogs"

inherit cmake pkgconfig

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lui_aura -lui_base -lskia -lbase'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/shell_dialogs
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/shell_dialogs
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
