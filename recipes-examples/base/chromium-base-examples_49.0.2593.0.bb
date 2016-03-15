SUMMARY = "Chromium Base examples"
DESCRIPTION = "Some examples files for the Chromium Base helper library."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base"

SRC_URI = " \
           file://LICENSE \
           file://CMakeLists.txt \
           file://chromium_base-1_log.cc \
           file://chromium_base-2_file.cc \
           file://chromium_base-3_cmdline.cc \
           file://chromium_base-4_callback.cc \
           file://chromium_base-5_loop.cc \
          "

inherit cmake

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase"

do_configure_prepend() {
       mkdir -p ${S}
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
       cp ${WORKDIR}/*.cc ${S}
}

FILES_${PN}-dbg += "${bindir}/chromium/.debug/*"
