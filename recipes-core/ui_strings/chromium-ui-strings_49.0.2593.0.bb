SUMMARY = "Chromium UI Strings"
DESCRIPTION = "Miscellaneous UI string resources."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "711f69a0086d231450c529ee1dc427dffd5f9256"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/strings"

inherit cmake pkgconfig

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

# we override the install step as our CMakeLists.txt does not have one
do_install() {
       # we need to copy generated headers living in the "build" directory
       cd ${B}/ui/strings
       mkdir -p ${D}${includedir}/chromium/ui/strings
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/strings
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${datadir}/chromium
       cp *.pak ${D}${datadir}/chromium
}

FILES_${PN} += "${datadir}/chromium/*.pak"
