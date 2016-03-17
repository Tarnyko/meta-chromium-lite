PROJECT(chromium-ui-events-examples CXX)

CMAKE_MINIMUM_REQUIRED(VERSION 2.8.8)
SET(CMAKE_BUILD_TYPE Debug)
SET(CMAKE_POSITION_INDEPENDENT_CODE ON)

SET(PROJECT_NAME "Chromium - UI Events examples")

# CMake modules
INCLUDE(GNUInstallDirs)

IF(CMAKE_COMPILER_IS_GNUCXX)
    ADD_DEFINITIONS(-std=c++11)
ENDIF()


ADD_EXECUTABLE(chromium_ui_events-1_mouse chromium_ui_events-1_mouse.cc)
INCLUDE_DIRECTORIES(${include_dirs})
TARGET_LINK_LIBRARIES(chromium_ui_events-1_mouse ${LINK_LIBRARIES})

INSTALL(TARGETS chromium_ui_events-1_mouse
        RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}/chromium)
