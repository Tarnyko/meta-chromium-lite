SUMMARY = "Minizip Compression Library"
DESCRIPTION = "Zlib is a general-purpose, patent-free, lossless data compression \
library which is used by many different programs. Minizip is a subpackage."
HOMEPAGE = "http://zlib.net/"
SECTION = "libs"
LICENSE = "Zlib"
LIC_FILES_CHKSUM = "file://zlib.h;beginline=4;endline=23;md5=fde612df1e5933c428b73844a0c494fd"

DEPENDS = "libtool-native"

SRC_URI = "http://www.zlib.net/zlib-${PV}.tar.xz"

SRC_URI[md5sum] = "28f1205d8dd2001f26fec1e8c2cebe37"
SRC_URI[sha256sum] = "831df043236df8e9a7667b9e3bb37e1fcb1220a0f163b6de2626774b9590d057"

S = "${WORKDIR}/zlib-${PV}"

do_configure (){
	cd contrib/minizip
	autoreconf --install
	./configure --prefix=${prefix} --shared=${datadir} --libdir=${libdir} --host=${HOST_SYS}
}

do_compile (){
	cd contrib/minizip
	oe_runmake
}

do_install() {
	cd contrib/minizip
	oe_runmake DESTDIR=${D} install
	# Remove .la files
	rm -f ${D}/${libdir}/*.la
}

BBCLASSEXTEND = "native nativesdk"
