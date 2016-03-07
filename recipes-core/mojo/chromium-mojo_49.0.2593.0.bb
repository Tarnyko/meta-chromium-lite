SUMMARY = "Chromium Mojo helper library"
DESCRIPTION = "An effort to isolate all components of the multi-process \
architecture concerned with process management, sandboxing and IPC."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-url chromium-crypto chromium-gin chromium-net python-native python-jinja2-native python-ply-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV = "cf59faa001ce1dc4ed51e86d1069346152db6cb4"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/${NAME}"

inherit cmake pkgconfig

OECMAKE_CXX_FLAGS_append = " -I${STAGING_INCDIR}/chromium"
OECMAKE_CXX_LINK_FLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lurl_lib -lcrcrypto -lgin -lnet"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       # we need to copy generated headers living in the "build" directory
       cd ${B}/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
