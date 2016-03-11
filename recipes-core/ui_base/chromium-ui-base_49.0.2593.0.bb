SUMMARY = "Chromium UI Base helper library"
DESCRIPTION = "Handles various UI logic, such as clipboard, cursor change, \
input method choice..."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-url chromium-net chromium-skia chromium-ui-gfx chromium-ui-events chromium-ui-strings chromium-ui-resources"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV = "5dd7421bdc4193b10032c0866d732f9ed27a6179"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/base"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'x11', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE"
PACKAGECONFIG[x11] = "-DBACKEND=X11,,virtual/libx11 libxcursor libxext libxfixes libxrender libxscrnsaver"

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lurl_lib -lnet -lskia -lui_gfx -lui_events -lui_resources"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/base
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/base
       cd ${S}/../ozone
       mkdir -p ${D}${includedir}/chromium/ui/ozone
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/ozone
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
