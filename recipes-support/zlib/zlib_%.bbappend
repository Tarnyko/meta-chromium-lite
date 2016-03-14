# Build additional "minizip" packages, required by "chromium-content"
# ------------------------------------------------------------------

DEPENDS += "libtool-native"
PROVIDES += "minizip"

do_configure_append (){
	cd contrib/minizip
	autoreconf --install
	./configure --prefix=${prefix} --shared=${datadir} --libdir=${libdir} --host=${HOST_SYS}
}

do_compile_append (){
	cd contrib/minizip
	oe_runmake
}

do_install_append (){
	cd contrib/minizip
	oe_runmake DESTDIR=${D} install
        # Remove .la files
        rm -f ${D}/${libdir}/*.la
}

PACKAGES += "minizip minizip-dbg minizip-dev minizip-staticdev"

FILES_${PN} = "${base_libdir}/libz.so.*"
FILES_${PN}-dbg = "${base_libdir}/.debug/libz.so.* ${prefix}/src/*"
FILES_${PN}-dev = "${includedir}/*.h ${libdir}/libz.so ${libdir}/pkgconfig/zlib.pc"
FILES_${PN}-staticdev = "${libdir}/libz.a"

FILES_minizip = "${libdir}/libminizip.so.*"
FILES_minizip-dbg = "${libdir}/.debug/libminizip.so.*"
FILES_minizip-dev = "${includedir}/minizip/* ${libdir}/libminizip.so ${libdir}/pkgconfig/minizip.pc"
FILES_minizip-staticdev = "${libdir}/libminizip.a"
