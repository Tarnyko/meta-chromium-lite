SUMMARY = "Chromium Storage helper library"
DESCRIPTION = "An abstraction layer for browser database storage; sits on top \
of SQL (SQLite helper library) and Net (network and browser helper library) \
and also provides an API for Google's NoSQL LevelDB engine."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-url chromium-sql chromium-net leveldb"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "85dd931215d6cb100eb5e6caa8627de4a8d07853"
SRCREV_leveldb-ext = "706b7f8d43b0aecdc75c5ee49d3e4ef5f27b9faf"
SRCREV_snappy = "762bb32f0c9d2f31ba4958c7c0933d22e80c20bf"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://chromium.googlesource.com/external/leveldb.git;protocol=https;name=leveldb-ext;destsuffix=git/third_party/leveldatabase/src \
           git://chromium.googlesource.com/external/snappy.git;protocol=https;name=snappy;destsuffix=git/third_party/snappy/src \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/${NAME}"

inherit cmake pkgconfig

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lurl_lib -lsql -lnet -lleveldb"

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
