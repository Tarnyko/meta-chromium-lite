PROJECT(chromium-base-examples CXX)

CMAKE_MINIMUM_REQUIRED(VERSION 2.8.8)
SET(CMAKE_BUILD_TYPE Debug)
SET(CMAKE_POSITION_INDEPENDENT_CODE ON)

SET(PROJECT_NAME "Chromium - Base examples")

# CMake modules
INCLUDE(GNUInstallDirs)

IF(CMAKE_COMPILER_IS_GNUCXX)
    ADD_DEFINITIONS(-std=c++11)
ENDIF()


ADD_EXECUTABLE(chromium_base-1_log chromium_base-1_log.cc)
ADD_EXECUTABLE(chromium_base-2_file chromium_base-2_file.cc)
ADD_EXECUTABLE(chromium_base-3_cmdline chromium_base-3_cmdline.cc)
ADD_EXECUTABLE(chromium_base-4_callback chromium_base-4_callback.cc)
ADD_EXECUTABLE(chromium_base-5_loop chromium_base-5_loop.cc)
INCLUDE_DIRECTORIES(${include_dirs})

INSTALL(TARGETS chromium_base-1_log chromium_base-2_file chromium_base-3_cmdline chromium_base-4_callback chromium_base-5_loop
        RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}/chromium)
