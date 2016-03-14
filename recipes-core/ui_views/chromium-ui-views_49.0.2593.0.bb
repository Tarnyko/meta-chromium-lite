SUMMARY = "Chromium UI Views library"
DESCRIPTION = "Views is the Chromium GUI toolkit: it implements windows, buttons, text fields, \
scrollbars... Controls are drawn by calling UI Gfx, which itself calls Skia at a lower level. \
When built with Content (as we do here), it provides a WebView Control, which is the browser \
widget and the main purpose of Chromium."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-url chromium-skia chromium-ui-gfx chromium-ui-accessibility chromium-ui-events chromium-ui-base chromium-ui-compositor chromium-ui-aura chromium-ui-shelldialogs chromium-ui-strings chromium-ui-resources python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "cde46612523cf0612ca563b563b6b090f3fffb98"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/views"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'x11', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE"
PACKAGECONFIG[x11] = "-DBACKEND=X11,,virtual/libx11 libxext libxrandr"

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lurl_lib -lskia -lui_gfx -lui_accessibility -lui_events -lui_base -lui_compositor -lui_aura -lui_shell_dialogs -lui_resources"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/views
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/views
       # Ozone already provides this for Wayland, thus only provide it under X11
       if ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'false', 'true', d)}; then
           cd ${S}/../display
           mkdir -p ${D}${includedir}/chromium/ui/display
           cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/display
       fi
       # we need to copy generated headers living in the "build" directory
       cd ${B}/ui/views
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${datadir}/chromium
       cp ${B}/ui/views/resources/*.pak ${D}${datadir}/chromium
}

FILES_${PN} += "${libdir}/chromium/*.so ${datadir}/chromium/*.pak"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
