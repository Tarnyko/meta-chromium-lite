SUMMARY = "Chromium UI Events helper library"
DESCRIPTION = "Provides input device (keyboard, mouse, touch) hardware \
detection as well as input event handling for the Chromium UI. Under X11, \
it uses the X server for this purpose; under Wayland, it directly requests \
udev for hardware detection, delegating input event handling to the Ozone \
helper library (which eventually handles Wayland)."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-ipc chromium-skia chromium-ui-gfx libxkbcommon"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "63179d49b117b071bf86462476e2e1361b4e5e3e"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/ui/events"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'x11', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE,,udev python-native"
PACKAGECONFIG[x11] = "-DBACKEND=X11,,virtual/libx11 libxi xproto inputproto libevent"

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lui_gfx -lskia -lipc -lbase'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/events
       cp --parents `find . -name "*.h" -o -name "*.inc"` ${D}${includedir}/chromium/ui/events
       cd ${S}/../ozone
       mkdir -p ${D}${includedir}/chromium/ui/ozone
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/ozone
       cd ${S}/../../device/udev_linux
       mkdir -p ${D}${includedir}/chromium/device/udev_linux
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/device/udev_linux
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
