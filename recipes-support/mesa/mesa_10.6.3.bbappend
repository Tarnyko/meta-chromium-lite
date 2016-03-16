FILESEXTRAPATHS_prepend := ":${THISDIR}/files:"

# this reverts "https://lists.freedesktop.org/archives/mesa-dev/2015-May/083999.html",
# which prevents Skia from linking in EGL(-Wayland) mode because it needs the following
# symbols exported : "eglDestroySyncKHR", "eglClientWaitSyncKHR", "eglCreateSyncKHR"
# (upstream Chromium does not care because it builds its own Mesa...)

SRC_URI_append = " \
                   file://revert-expose_only_core_EGL_functions_statically.patch \
                 "
