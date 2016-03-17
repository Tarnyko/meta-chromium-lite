SUMMARY = "Chromium Content library"
DESCRIPTION = "Content is Chromium's real browser implementation, which \
initializes the Blink web engine, leverages CC to merge Blink rendering and \
Views/UI Gfx rendering in a single canvas... more globally links together all \
browser logic found in various subcomponents."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-url chromium-gin chromium-net chromium-mojo chromium-storage chromium-ipc chromium-skia chromium-ui-gfx chromium-ui-accessibility chromium-ui-events chromium-media chromium-ui-base chromium-ui-gl chromium-gpu chromium-cc chromium-blink chromium-ui-events-blink chromium-gpu-blink chromium-cc-blink chromium-ui-aura chromium-ui-touchselection chromium-ui-snapshot chromium-ui-shelldialogs chromium-media-blink nss minizip protobuf protobuf-native python-native python-ply-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "120a23dc83f048ffa766bfdb3acf1d4e7f8a44cd"
SRCREV_build = "1aa26aaba531135f3ca9ffd522bffb3f7b8f1be6"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRCREV_mojo = "01c4c8813edc16161c19eb98367e9237ce193432"
SRCREV_webrtc = "71ae2ea48a6b6c42eb79bfd70acf888056b6a711"
SRCREV_catapult = "506457cbd726324f327b80ae11f46c1dfeb8710d"
SRCREV_flac = "2c4b86af352b23498315c016dc207e3fb2733fc0"
SRCREV_libjingle = "b7037034bc843dd1e7ff8a3b40439f8ead2dd823"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-build.git;name=build;destsuffix=git/build \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           git://github.com/Tarnyko/chromium-mojo.git;name=mojo;destsuffix=git/mojo \
           git://chromium.googlesource.com/external/webrtc/trunk/webrtc.git;protocol=https;name=webrtc;destsuffix=git/third_party/webrtc \
           git://github.com/catapult-project/catapult.git;name=catapult;destsuffix=git/third_party/catapult \
           git://chromium.googlesource.com/chromium/deps/flac.git;protocol=https;name=flac;destsuffix=git/third_party/flac \
           git://chromium.googlesource.com/external/webrtc/trunk/talk.git;protocol=https;name=libjingle;destsuffix=git/third_party/libjingle/source/talk \
           file://LICENSE \
           file://CMakeLists.txt \
           file://disable_battery_vibration_bluetooth.patch \
           file://disable_speech_recognition.patch \
           file://renderer_disable_battery_vibration_bluetooth_usb.patch \
           file://sandbox_linux_x86_disable_seccomp_bpf.patch \
           file://zygote_sandbox_use_nss.patch \
           file://OZONE_disable_mediaozoneplatform.patch \
           file://z_disable_client_cert.patch \
           file://z_disable_webmediaplayerimpl.patch \
           file://z_time_zone_monitor_noicufork.patch \
          "

S = "${WORKDIR}/git/${NAME}"

inherit cmake pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'x11', d)}"
PACKAGECONFIG[wayland] = "-DBACKEND=OZONE"
PACKAGECONFIG[x11] = "-DBACKEND=X11,,virtual/libx11 libxcomposite pango"

# Content needs a path to various Blink resources files
EXTRA_OECMAKE_append = " -DINCDIR:PATH=${STAGING_INCDIR}/chromium -DDATADIR:PATH=${STAGING_DATADIR}/chromium"

# The first paths are for generated headers (Mojo, Skia, UI Gfx & CC) having relative paths
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium -I${STAGING_INCDIR}/chromium/mojo -I${STAGING_INCDIR}/chromium/skia -I${STAGING_INCDIR}/chromium/ui -I${STAGING_INCDIR}/chromium/cc -I${STAGING_INCDIR}/chromium/skia/config -I${STAGING_INCDIR}/chromium/third_party/skia/include/core -I${STAGING_INCDIR}/chromium/third_party/skia/include/utils -I${STAGING_INCDIR}/chromium/third_party/skia/include/gpu -I${STAGING_INCDIR}/chromium/third_party/WebKit -I${STAGING_INCDIR}/chromium/third_party/leveldatabase/src -I${STAGING_INCDIR}/chromium/third_party/leveldatabase/src/include -I${STAGING_INCDIR}/chromium/third_party/angle/src -I${STAGING_INCDIR}/chromium/third_party/mesa/src/include -I${STAGING_INCDIR}/chromium/v8/include -I${STAGING_INCDIR}/chromium/third_party/libyuv/include"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lmedia_blink -lui_shell_dialogs -lui_snapshot -lui_touch_selection -lui_aura -lcc_blink -lgpu_blink -lui_events_blink -lblink -lcc -lgpu -lui_gl -lui_base -lmedia -lui_events -lui_accessibility -lui_gfx -lskia -lipc -lstorage -lmojo -lnet -lgin -lurl_lib -lbase'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
       # we apply these patches separately because they live in the source tree
       cd ${S}/..
       patch -f -p1 < url_formatter_noicufork.patch || true
       patch -f -p1 < webcrypto_use_nss.patch || true
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       cd ${S}/../components
       mkdir -p ${D}${includedir}/chromium/components
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/components
       cd ${S}/../sandbox
       mkdir -p ${D}${includedir}/chromium/sandbox
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/sandbox
       cd ${S}/../ui/surface
       mkdir -p ${D}${includedir}/chromium/ui/surface
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/ui/surface
       cd ${S}/../third_party/webrtc
       mkdir -p ${D}${includedir}/chromium/third_party/webrtc
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/webrtc
       cd ${S}/../third_party/flac
       mkdir -p ${D}${includedir}/chromium/third_party/flac
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/flac
       cd ${S}/../third_party/libjingle
       mkdir -p ${D}${includedir}/chromium/third_party/libjingle
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/libjingle
       cd ${S}/../third_party/npapi
       mkdir -p ${D}${includedir}/chromium/third_party/npapi
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/npapi
       # we need to copy generated headers living in the "build" directory
       cd ${B}/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       cd ${B}/blink
       mkdir -p ${D}${includedir}/chromium/blink
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/blink
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${datadir}/chromium
       cd ${B}/${NAME}
       find . -name "*.pak" | xargs -i cp {} ${D}${datadir}/chromium
       cd ${B}/blink
       find . -name "*.pak" | xargs -i cp {} ${D}${datadir}/chromium
}

FILES_${PN} += "${libdir}/chromium/*.so ${datadir}/chromium/*.pak"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
