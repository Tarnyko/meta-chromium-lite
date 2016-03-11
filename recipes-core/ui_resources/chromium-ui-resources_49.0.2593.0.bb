SUMMARY = "Chromium UI Resources"
DESCRIPTION = "Miscellaneous UI resources."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "ccc9f02079f177272df7dd69fb31fa2deb574c9a"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/resources"

inherit cmake pkgconfig

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       # we need to copy generated headers living in the "build" directory
       cd ${B}/ui/resources
       mkdir -p ${D}${includedir}/chromium/ui/resources
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/resources
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${datadir}/chromium
       cp *.pak ${D}${datadir}/chromium
}

FILES_${PN} += "${libdir}/chromium/*.so ${datadir}/chromium/*.pak"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
