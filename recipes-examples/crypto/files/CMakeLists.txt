PROJECT(chromium-crypto-examples CXX)

CMAKE_MINIMUM_REQUIRED(VERSION 2.8.8)
SET(CMAKE_BUILD_TYPE Debug)
SET(CMAKE_POSITION_INDEPENDENT_CODE ON)

SET(PROJECT_NAME "Chromium - Cryptographic examples")

# CMake modules
INCLUDE(FindPkgConfig)
INCLUDE(GNUInstallDirs)

PKG_CHECK_MODULES(nss REQUIRED nss)

IF(CMAKE_COMPILER_IS_GNUCXX)
    ADD_DEFINITIONS(-std=c++11)
ENDIF()


ADD_EXECUTABLE(chromium_crypto-1_random chromium_crypto-1_random.cc)
ADD_EXECUTABLE(chromium_crypto-2_encrypt chromium_crypto-2_encrypt.cc)
INCLUDE_DIRECTORIES(${include_dirs} ${nss_INCLUDE_DIRS})
TARGET_LINK_LIBRARIES(chromium_crypto-1_random ${LINK_LIBRARIES})
TARGET_LINK_LIBRARIES(chromium_crypto-2_encrypt ${LINK_LIBRARIES})

INSTALL(TARGETS chromium_crypto-1_random chromium_crypto-2_encrypt
        RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}/chromium)
