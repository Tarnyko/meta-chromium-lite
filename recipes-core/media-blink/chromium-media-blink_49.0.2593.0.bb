SUMMARY = "Chromium Media library - Blink additions"
DESCRIPTION = "Blink web engine additions for the Chrome Media library."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-url chromium-net chromium-skia chromium-ui-gfx chromium-media chromium-gpu chromium-cc chromium-blink chromium-gpu-blink chromium-cc-blink"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "afcab6c703822a07e088a45685bcb02f4a7c1d77"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           file://LICENSE \
           file://CMakeLists.txt \
          "

S = "${WORKDIR}/git/media/blink"

inherit cmake pkgconfig

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu -I${STAGING_INCDIR}/chromium/third_party/WebKit"
LDFLAGS_append = " -L${STAGING_LIBDIR}/chromium -lbase -lurl_lib -lnet -lskia -lui_gfx -lmedia -lgpu -lcc -lblink -lcc_blink -lgpu_blink"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/media/blink
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/media/blink
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
