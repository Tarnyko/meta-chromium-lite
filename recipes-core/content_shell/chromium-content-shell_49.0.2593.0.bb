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

SRCREV_${NAME} = "46062afd5685ec14274d65444e9517bdb423a59d"
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
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/mojo -I${STAGING_INCDIR}/chromium/content -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu -I${STAGING_INCDIR}/chromium/third_party/WebKit -I${STAGING_INCDIR}/chromium/v8/include -DUSE_AURA"
CXXFLAGS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', ' -DUSE_OZONE', ' -DUSE_X11', d)}"
CXXFLAGS_remove = "-fvisibility-inlines-hidden"
LDFLAGS_append = " -Wl,-rpath-link,${STAGING_LIBDIR}/chromium -Wl,-rpath,${libdir}/chromium"
LDFLAGS_remove = " -Wl,--as-needed"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lui_views -lui_web_dialogs -lui_content_accelerators -lcontent -lmedia_blink -lui_shell_dialogs -lui_snapshot -lui_touch_selection -lui_aura -lui_compositor -lui_events_blink -lcc_blink -lgpu_blink -lblink -lcc -lgpu -lui_gl -lmedia -lui_base ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '-lozone', '', d)} -lui_events -lui_accessibility -lui_gfx -lskia -lipc -lstorage -lmojo -lui_resources -lnet -lgpu_command_buffer -lgin -lsql -lcrcrypto -lurl_lib -lv8 -lbase -lgio-2.0'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/content/shell
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/content/shell
       # we need to copy generated headers living in the "build" directory
       cd ${B}/grit
       mkdir -p ${D}${includedir}/chromium/content/shell/grit
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/content/shell/grit
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${datadir}/chromium
       cp ${B}/*.pak ${D}${datadir}/chromium
}

FILES_${PN} += "${bindir}/chromium/content_shell ${datadir}/chromium/*"
FILES_${PN}-dbg += "${bindir}/chromium/.debug/*"
