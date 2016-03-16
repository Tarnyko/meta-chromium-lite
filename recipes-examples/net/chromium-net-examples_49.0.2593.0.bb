SUMMARY = "Chromium Net examples"
DESCRIPTION = "Some examples files for the Chromium Net library."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-net"

SRC_URI = " \
           file://LICENSE \
           file://CMakeLists.txt \
           file://chromium_net-1_http.cc \
          "

inherit cmake

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"
LDFLAGS_append = " -Wl,-rpath-link,${STAGING_LIBDIR}/chromium"
LDFLAGS_remove = " -Wl,--as-needed"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lnet -lurl_lib -lbase'"


do_configure_prepend() {
       mkdir -p ${S}
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
       cp ${WORKDIR}/*.cc ${S}
}

FILES_${PN}-dbg += "${bindir}/chromium/.debug/*"
