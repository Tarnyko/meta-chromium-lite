SUMMARY = "Chromium Gin helper library"
DESCRIPTION = "A set of utility functions on top of V8; mostly used by the Blink web engine."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-v8 chromium-crypto"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV = "9f9b7b387988ba7288383f2b6a1d98e2ab9054e3"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/${NAME}"

inherit cmake pkgconfig

OECMAKE_CXX_FLAGS_append = " -I${STAGING_INCDIR}/chromium"
OECMAKE_CXX_LINK_FLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lv8 -lcrcrypto"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
