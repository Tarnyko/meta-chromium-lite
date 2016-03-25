SUMMARY = "Chromium UI Views library"
DESCRIPTION = "Views is the Chromium GUI toolkit: it implements windows, buttons, text fields, \
scrollbars... Controls are drawn by calling UI Gfx, which itself calls Skia at a lower level. \
When built with Content (as we do here), it provides a WebView Control, which is the browser \
widget and the main purpose of Chromium."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-url chromium-skia chromium-ui-gfx chromium-ui-accessibility chromium-ui-events chromium-ui-base chromium-ui-compositor chromium-ui-aura chromium-ui-shelldialogs chromium-content chromium-ui-contentaccelerators chromium-ui-webdialogs chromium-ui-strings chromium-ui-resources python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "afc2a4cc8ff4b090b0ce3efa0338fe0b5b504e1e"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRCREV_ozone-wayland = "9816917277df5defa2d2787aeea3c857465714dc"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           git://github.com/01org/ozone-wayland.git;name=ozone-wayland;destsuffix=git/ozone \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/views"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'x11', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE"
PACKAGECONFIG[x11] = "-DBACKEND=X11,,virtual/libx11 libxext libxrandr"

CXXFLAGS_remove = "-fvisibility-inlines-hidden"
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lui_resources -lui_shell_dialogs -lui_aura -lui_compositor -lui_base -lui_events -lui_accessibility -lui_gfx -lskia -lurl_lib -lbase'"

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
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/views
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${datadir}/chromium
       cp ${B}/ui/views/resources/*.pak ${D}${datadir}/chromium
}

FILES_${PN} += "${libdir}/chromium/*.so ${datadir}/chromium/*.pak"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
