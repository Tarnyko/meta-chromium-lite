PROJECT(chromium-content-shell-pak CXX)

CMAKE_MINIMUM_REQUIRED(VERSION 2.8.8)
SET(CMAKE_BUILD_TYPE Debug)
SET(CMAKE_POSITION_INDEPENDENT_CODE ON)

SET(PROJECT_NAME "Chromium - Content Shell PAK")

# Mandatory dependencies
#FIND_PROGRAM(PYTHON_FOUND python)
#IF(NOT PYTHON_FOUND)
#  MESSAGE(FATAL_ERROR "\"python\" is required, please install it !")
#ELSE()
#  MESSAGE(STATUS "\"python\" found...")
#ENDIF(NOT PYTHON_FOUND)


MESSAGE(STATUS "Generating content_shell.pak...")

# the ${DATADIR} value is passed by the parent build system
EXECUTE_PROCESS(COMMAND python grit/grit/format/repack.py ${CMAKE_BINARY_DIR}/content_shell.pak ${DATADIR}/blink_resources.pak ${DATADIR}/blink_image_resources_100_percent.pak ${DATADIR}/content_resources_100_percent.pak ${DATADIR}/content_strings_en-US.pak ${DATADIR}/tracing_resources.pak ${DATADIR}/content_resources.pak ${DATADIR}/shell_resources.pak ${DATADIR}/net_resources.pak ${DATADIR}/ui_resources_100_percent.pak ${DATADIR}/webui_resources.pak ${DATADIR}/app_locale_settings_en-US.pak ${DATADIR}/ui_strings_en-US.pak ${DATADIR}/views_resources_100_percent.pak ${DATADIR}/devtools_resources.pak WORKING_DIRECTORY ${CMAKE_SOURCE_DIR})
