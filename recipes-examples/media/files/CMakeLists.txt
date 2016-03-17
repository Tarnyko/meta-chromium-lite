PROJECT(chromium-media-examples CXX)

CMAKE_MINIMUM_REQUIRED(VERSION 2.8.8)
SET(CMAKE_BUILD_TYPE Debug)
SET(CMAKE_POSITION_INDEPENDENT_CODE ON)

SET(PROJECT_NAME "Chromium - Media examples")

# CMake modules
INCLUDE(GNUInstallDirs)

IF(CMAKE_COMPILER_IS_GNUCXX)
    ADD_DEFINITIONS(-std=c++11)
ENDIF()


ADD_EXECUTABLE(chromium_media-1_audio_config chromium_media-1_audio_config.cc)
INCLUDE_DIRECTORIES(${include_dirs})
TARGET_LINK_LIBRARIES(chromium_media-1_audio_config ${LINK_LIBRARIES})

INSTALL(TARGETS chromium_media-1_audio_config
        RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}/chromium)
