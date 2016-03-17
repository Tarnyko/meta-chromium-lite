PROJECT(chromium-ui-gl-examples CXX)

CMAKE_MINIMUM_REQUIRED(VERSION 2.8.8)
SET(CMAKE_BUILD_TYPE Debug)
SET(CMAKE_POSITION_INDEPENDENT_CODE ON)

SET(PROJECT_NAME "Chromium - UI GL examples")

# CMake modules
INCLUDE(GNUInstallDirs)

IF(CMAKE_COMPILER_IS_GNUCXX)
    ADD_DEFINITIONS(-std=c++11)
ENDIF()


ADD_EXECUTABLE(chromium_ui_gl-1_gpu_timing chromium_ui_gl-1_gpu_timing.cc)
INCLUDE_DIRECTORIES(${include_dirs})
TARGET_LINK_LIBRARIES(chromium_ui_gl-1_gpu_timing ${LINK_LIBRARIES})

INSTALL(TARGETS chromium_ui_gl-1_gpu_timing
        RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}/chromium)
