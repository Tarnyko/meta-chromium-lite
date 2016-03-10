SUMMARY = "Chromium UI Accessibility helper library"
DESCRIPTION = "Implements various internationalization-related UI logic, \
including left-to-right and right-to-left text handling."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-ui-gfx gconf atk python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "f367ded53a428ae6a3c6a76bb8b4adc0099aca13"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           file://LICENSE \
           file://CMakeLists.txt \
           file://OZONE_provide_axplatformnodeauralinux.patch \
          "

S = "${WORKDIR}/git/ui/accessibility"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE,-DBACKEND=X11"

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lui_gfx"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/accessibility
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/accessibility
       # we need to copy generated headers living in the "build" directory
       cd ${B}/ui/accessibility
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/accessibility
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
