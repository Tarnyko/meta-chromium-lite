SUMMARY = "Chromium Net helper library"
DESCRIPTION = "The core of Chromium network protocol handling (http://, \
ftp://, file://, SPDY, HTTP/2, WebSockets...). Also provides various \
browser-oriented mechanisms such as cache and cookies management, DNS \
resolving, proxy settings, SSL certificate validation..."
HOMEPAGE = "https://www.chromium.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := ":${THISDIR}/../../shared:"

DEPENDS = "chromium-base chromium-url chromium-crypto chromium-sql nss nspr open-vcdiff protobuf protobuf-native python-native"

NAME = "${@'${BPN}'.replace('chromium-', '')}"

SRCREV_${NAME} = "f424ccb17dd659adc7aecda813a5f4112e17c14f"
SRCREV_tools = "a5bb4ed0080f1f0940b994875020e4f6b8aca0c6"
SRCREV_open-vcdiff = "21d7d0b9c3d0c3ccbdb221c85ae889373f0a2a58"
SRC_URI = " \
           git://github.com/Tarnyko/chromium-${NAME}.git;name=${NAME} \
           git://github.com/Tarnyko/chromium-tools.git;name=tools;destsuffix=git/tools \
           git://github.com/google/open-vcdiff.git;name=open-vcdiff;destsuffix=git/sdch/open-vcdiff \
           file://LICENSE \
           file://CMakeLists.txt \
           file://des_supportnss.patch \
           file://ftp_util_noicufork.patch \
           file://quic_nochacha.patch \
          "

S = "${WORKDIR}/git/${NAME}"

inherit cmake pkgconfig

FULL_OPTIMIZATION = ""
CXXFLAGS_remove = "-fvisibility-inlines-hidden"
CXXFLAGS_append = " -I${STAGING_INCDIR}/chromium"
EXTRA_OECMAKE_append = " -DLINK_LIBRARIES='-L${STAGING_LIBDIR}/chromium -lsql -lcrcrypto -lurl_lib -lbase'"

do_configure_prepend() {
       cp ${WORKDIR}/LICENSE ${S}
       cp ${WORKDIR}/CMakeLists.txt ${S}
       # we apply this patch separately because it is outside of the source tree
       cd ${S}/..
       patch -f -p1 < zlib_no_MOZ_prefix.patch || true
}

do_install_append() {
       cd ${S}
       mkdir -p ${D}${includedir}/chromium/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       cd ${S}/../third_party/zlib
       mkdir -p ${D}${includedir}/chromium/third_party/zlib
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/third_party/zlib
       # we need to copy generated headers living in the "build" directory
       cd ${B}/${NAME}
       cp --parents `find . -name "*.h"` ${D}${includedir}/chromium/${NAME}
       # we need to copy generated .pak files living in the "build" directory
       mkdir -p ${D}${datadir}/chromium
       cd ${B}/${NAME}
       cp *.pak ${D}${datadir}/chromium
}

FILES_${PN} += "${libdir}/chromium/*.so ${datadir}/chromium/*.pak"
FILES_${PN}-dbg += "${libdir}/chromium/.debug/*"
