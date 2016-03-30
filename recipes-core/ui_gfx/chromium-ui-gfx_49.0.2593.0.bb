SUMMARY = "Chromium UI Gfx helper library"
DESCRIPTION = "An intermediate drawing API used by the high-level Views \
toolkit to draw widgets, by calling the low-level Skia library. Can easily \
 convert between PNG/JPEG formats, draw text, set canvases bounds..."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-mojo chromium-ipc chromium-skia harfbuzz python-native python-ply-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "008e3ffbda5d3e7c62b5a155175334d8a961a0ef"
SRCREV_mojo = "01c4c8813edc16161c19eb98367e9237ce193432"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-mojo.git;name=mojo;destsuffix=git/mojo \
           file://LICENSE \
           file://CMakeLists.txt \
           file://OZONE_support_nativeviewaccessible.patch \
          "

S = "${WORKDIR}/git/ui/gfx"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE,-DBACKEND=X11"

FULL_OPTIMIZATION = ""
CXXFLAGS_remove = "-fvisibility-inlines-hidden"
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/mojo -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lskia -lipc -lmojo -lbase'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/ui/gfx
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/gfx
       cd ${S}/../../third_party/harfbuzz-ng
       mkdir -p ${D}${includedir}/chromium/third_party/harfbuzz-ng
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/harfbuzz-ng
       # this is required to build Content
       cd ${S}/../mojo/geometry
       mkdir -p ${D}${includedir}/chromium/ui/mojo/geometry
       cp --parents `find . -name "*.mojom"` ${D}${includedir}/chromium/ui/mojo/geometry
       # we need to copy generated headers living in the "build" directory
       cd ${B}/ui/gfx
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/gfx
       cd ${B}/ui/mojo
       mkdir -p ${D}${includedir}/chromium/ui/mojo
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/mojo
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
