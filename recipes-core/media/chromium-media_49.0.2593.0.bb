SUMMARY = "Chromium Media library"
DESCRIPTION = "Provides audio playback (with ALSA, PulseAudio), video playback ,\
codec interface. Currently, PulseAudio and FFmpeg are disabled."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-url chromium-crypto chromium-gpu-commandbuffer chromium-skia chromium-ui-gfx chromium-ui-events chromium-ui-gl libjpeg-turbo alsa-lib libopus"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "5ed40694f3562902b1099924fbd9dafd1a14d91a"
SRCREV_cdm = "a4773c3cec827c3a880e8a2c22e43a287ced0d20"
SRCREV_libyuv = "1019e4537fc1bfc6ee505cd1c628b645c7e966b7"
SRCREV_libwebm = "75a6d2da8b63e0c446ec0ce1ac942c2962d959d7"
SRCREV_opus = "cae696156f1e60006e39821e79a1811ae1933c69"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://chromium.googlesource.com/chromium/cdm.git;protocol=https;name=cdm;destsuffix=git/media/cdm/api \
           git://chromium.googlesource.com/libyuv/libyuv.git;protocol=https;name=libyuv;destsuffix=git/third_party/libyuv \
           git://chromium.googlesource.com/webm/libwebm.git;protocol=https;name=libwebm;destsuffix=git/third_party/libwebm/source \
           git://chromium.googlesource.com/chromium/deps/opus.git;protocol=https;branch=git-svn;name=opus;destsuffix=git/third_party/opus/src \
           file://LICENSE \
           file://CMakeLists.txt \
           file://Toolchain-arm.cmake \
           file://Toolchain-x86.cmake \
           file://Toolchain-x86_64.cmake \
          "

S = "${WORKDIR}/git/${NAME}"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'x11', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE"
PACKAGECONFIG[x11] = "-DBACKEND=X11,,virtual/libx11 libxdamage libxext libxfixes libxtst"

DEPENDS_append_i586 = " yasm-native"
DEPENDS_append_i686 = " yasm-native"
DEPENDS_append_x86_64 = " yasm-native"
EXTRA_OECMAKE_append_arm = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-arm.cmake"
EXTRA_OECMAKE_append_i586 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86.cmake"
EXTRA_OECMAKE_append_i686 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86.cmake"
EXTRA_OECMAKE_append_x86_64 = " -DCMAKE_TOOLCHAIN_FILE=../Toolchain-x86_64.cmake"

CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lui_gl -lui_events -lui_gfx -lskia -lgpu_command_buffer -lcrcrypto -lurl_lib -lbase'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       cd ${S}/../third_party/libwebm
       mkdir -p ${D}${includedir}/chromium/third_party/libwebm
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/libwebm
       cd ${S}/../third_party/libyuv
       mkdir -p ${D}${includedir}/chromium/third_party/libyuv
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/libyuv
}

FILES_${PN} += "${libdir}/chromium/*.so"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
