SUMMARY = "Chromium Content Shell"
DESCRIPTION = "Content Shell is a minimal browser designed for testing purposes; normally able \
to execute a series of layout tests, we disable this features to avoid dependencies on the \
GTest/GMock framework. It uses Views to display an address bar, Back/Previous/Refresh buttons, \
and a right-click contextual menu."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-v8 chromium-url chromium-net chromium-mojo chromium-ipc chromium-skia chromium-ui-gfx chromium-ui-accessibility chromium-blink chromium-ui-aura chromium-content chromium-ui-views python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "542190fd53c59ff8e25a623c3ee3c735f40c7b88"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           file://LICENSE \
           file://CMakeLists.txt \
           file://disable_bluetooth.patch \
           file://disable_tests_breakpad.patch \
          "

S = "${WORKDIR}/git/content/shell"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE,-DBACKEND=X11"

# The first paths are for generated headers (Mojo, Content) having relative paths
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/mojo -I${STAGING_INCDIR}/chromium/content -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu -I${STAGING_INCDIR}/chromium/third_party/WebKit -I${STAGING_INCDIR}/chromium/v8/include"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lv8 -lurl_lib -lcrcrypto -lsql -lgin -lgpu_command_buffer -lnet -lui_resources -lmojo -lstorage -lipc -lskia -lui_gfx -lui_accessibility -lui_events -lozone -lui_base -lmedia -lui_gl -lgpu -lcc -lblink -lgpu_blink -lcc_blink -lui_events_blink -lui_compositor -lui_aura -lui_touch_selection -lui_snapshot -lui_shell_dialogs -lmedia_blink -lcontent -lui_content_accelerators -lui_web_dialogs -lui_views"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/content/shell
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/content/shell
       # we need to copy generated headers living in the "build" directory
       cd ${B}/content/shell
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/content/shell
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${datadir}/chromium
       cp ${B}/content/shell/resources/*.pak ${D}${datadir}/chromium
}

FILES_${PN} += "${libdir}/chromium/*.so ${datadir}/chromium/*.pak"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
