SUMMARY = "Chromium UI Content Accelerators helper library"
DESCRIPTION = "Some additional accelerating functions for the Content library."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-ui-events chromium-ui-base chromium-blink chromium-content"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "42b0f299bd808e32ca7b909e37ff6b1a79f5d338"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/content_accelerators"

inherit cmake pkgconfig

CXXFLAGS_remove = "-fvisibility-inlines-hidden"
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lcontent -lblink -lui_base -lui_events'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/content_accelerators
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/content_accelerators
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
