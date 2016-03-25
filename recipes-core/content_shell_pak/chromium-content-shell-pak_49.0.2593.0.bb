SUMMARY = "Chromium Content Shell PAK file"
DESCRIPTION = "A compilation of resources coming from various dependencies, \
necessary to run Content Shell as a standalone browser."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-content-shell python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-tools.git \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git"

inherit cmake pkgconfig

# Producing "content_shell.pak" requires paths to various resources files
EXTRA_OECMAKE_append = " -DDATADIR:PATH=${STAGING_DATADIR}/chromium"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

# we override the install step as our CMakeLists.txt does not have one
do_install() {
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${bindir}/chromium
       cp ${B}/*.pak ${D}${bindir}/chromium
}

FILES_${PN} += "${bindir}/chromium/*.pak"
