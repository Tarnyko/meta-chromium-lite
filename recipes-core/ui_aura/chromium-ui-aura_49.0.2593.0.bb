SUMMARY = "Chromium UI Aura library"
DESCRIPTION = "Aura is the Chromium window manager; responsible for full \
"desktop" management under ChromeOS, it limits under GNU/Linux its role to \
dispatching events, managing Z-order... between Views windows (mostly when, \
for instance, dialog and popup windows are displayed)."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-skia chromium-ui-gfx chromium-ui-events chromium-ui-base chromium-ui-compositor chromium-ui-resources"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "6818a82795e34172f0ed6eb01deea10bdee47040"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/aura"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'x11', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE"
PACKAGECONFIG[x11] = "-DBACKEND=X11,,virtual/libx11 libxi libxrandr"

FULL_OPTIMIZATION = ""
CXXFLAGS_remove = "-fvisibility-inlines-hidden"
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lui_resources -lui_compositor -lui_base -lui_events -lui_gfx -lskia -lbase'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
       # we may need to apply Ozone-Wayland specific patches
       if ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'true', 'false', d)}; then
           cd ${S}/../..
           patch -f -p1 < OZONE-0007-Add-needed-support-in-PlatformWindow.patch || true
           patch -f -p1 < OZONE-0013-Add-drag-and-drop-interfaces-to-PlatformWindowDelega.patch || true
       fi
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/aura
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/aura
       cd ${S}/../aura_extra
       mkdir -p ${D}${includedir}/chromium/ui/aura_extra
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/aura_extra
       cd ${S}/../wm
       mkdir -p ${D}${includedir}/chromium/ui/wm
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/wm
       cd ${S}/../native_theme
       mkdir -p ${D}${includedir}/chromium/ui/native_theme
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/native_theme
       cd ${S}/../platform_window
       mkdir -p ${D}${includedir}/chromium/ui/platform_window
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/platform_window
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
