SUMMARY = "General-purpose Chromium toolkit"
DESCRIPTION = "Handles logging, callbacks, string and numeral conversions, \
command line arguments, file and folder operations, message loops, threads, \
time and date..."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "libevent icu python"

SRCREV_${BPN} = "19e64b167ab02b06f0af0f94d326e154c321a6f9"
SRCREV_build = "1aa26aaba531135f3ca9ffd522bffb3f7b8f1be6"
SRCREV_testing = "341033c3e485025968d9cee5a7372eb678d35341"
SRCREV_icu = " 8d342a405be5ae8aacb1e16f0bc31c3a4fbf26a2"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${BPN}.git;name=${BPN} \
           git://github.com/Tarnyko/chromium-build.git;name=build;destsuffix=git/build \
           git://github.com/Tarnyko/chromium-testing.git;name=testing;destsuffix=git/testing \
           git://github.com/Tarnyko/chromium-icu.git;name=icu;destsuffix=git/third_party/icu \
           file://LICENSE \
           file://CMakeLists.txt \
           file://time_formatting_noicufork.patch \
          "

S = "${WORKDIR}/git/${BPN}"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE,-DBACKEND=X11"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}
