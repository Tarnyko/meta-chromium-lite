SUMMARY = "Chromium Cryptographic helper library"
DESCRIPTION = "Handles hashing, randomization and encryption. It can use 2 \
implementations: Google BoringSSL (OpenSSL fork) or Mozilla NSS; we build \
NSS because it is widespread as system libraries."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base nss"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV = "e8919c2ba0f71d6a557f22a9ca5cf80375a0b513"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git \
           file://LICENSE \
           file://CMakeLists.txt \
           file://nss_util_backport_loadnsslibraries.patch \
          "

S = "${WORKDIR}/git/${NAME}"

inherit cmake pkgconfig

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase"

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
